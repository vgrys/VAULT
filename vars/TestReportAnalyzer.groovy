import java.text.DecimalFormat
import hudson.FilePath
import jenkins.model.Jenkins

JOB_ROOT = 'DIME/test_automation/Neptune/'
JOB_NAMES = ['CQ6_Automated_Testing' : '_1', 'CQ6_Automated_Testing_2' : '_2', 'CQ6_Automated_Testing_3' : '_3']
BUILD_NAMES = ['regression':'cq6_Sust_Regression', 'smoke':'cq6_SmokeTestsSust']

CSS_LINK = 'http://ajcp-jenkins01.corp.ad.ctc:8080/job/DIME/job/test_automation/job/Neptune/job/CQ6_Automated_Testing/803/HTML_Report/Full_Report/html/css/reportng.css'

PRIORITIES = ['High':1,'Medium':2,'Low':3]

def getJobItems() {
    return JOB_NAMES.keySet().collect { Jenkins.instance.getItemByFullName(JOB_ROOT + it) }
}

def getJobLastBuilds(jobItems, buildName) {
    return jobItems.collect { getPreviousSuccessfulBuild(it, getBuildFullName(buildName, it), 0) }
}

def getJobPrevBuilds(lastBuilds) {
    return lastBuilds.collect { getPreviousSuccessfulBuild(it.getParent(), it.getDisplayName(), it.getNumber()) }
}

def getPreviousSuccessfulBuild(job, buildName, lastBuildNum) {
    def builds = job.getBuilds()
    for (i = 0; i < builds.size(); i++)  {
        def lastBuild = builds[i]
        if ((lastBuildNum == 0 || lastBuild.getNumber() < lastBuildNum) && (lastBuild.getDisplayName() == buildName) && isBuildOK(lastBuild)) {
            return lastBuild
        }
    }
}

def getBuildFullName(buildName, job) {
    return buildName + getSuffix(job, buildName)
}

def getSuffix(job, buildName) {
    return buildName.contains('Smoke') ? '' : JOB_NAMES[job.getName()]
}

def isBuildOK(lastBuild) {
    def result = lastBuild.result.toString()
    return result == 'SUCCESS' || result == 'UNSTABLE'
}

def convertToPaths(builds) {
    return builds.collect { it.getRootDir().toString() + '/htmlreports/HTML_Report/Full_Report/html/overview.html'}
}

def writeToFile(filename, content) {
    def fp = null
    if(build.workspace.isRemote())  {
        channel = build.workspace.channel
        fp = new FilePath(channel, build.workspace.toString() + "/$filename")
    } else {
        fp = new FilePath(new File(build.workspace.toString() + "/$filename"))
    }

    if(fp != null)  {
        if (fp.exists()) fp.delete()
        fp.write(content, null) //writing to file
    }
}

def buildDocument(failedTestsRecords, newFailedTestsRecords, failsPercent, summary) {
    def sw = new StringWriter()
    def html = new groovy.xml.MarkupBuilder(sw)
    html.html {
        head {
            title('Tests analysis')
            link( [ 'href': CSS_LINK, 'rel':'stylesheet', 'type':'text/css' ])
        }
        body {
            h1('New failed tests in last run')
            newFailedTestsRecords.each { head, tests ->
                table('class':'overviewTable sort table-bordered') {
                    mkp.yieldUnescaped head
                    tbody {
                        tests.each {
                            mkp.yieldUnescaped it
                        }
                    }
                }
            }
            h1('All failed tests in last run. ' + failsPercent + '% of total.')
            failedTestsRecords.each { head, tests ->
                table('class':'overviewTable sort table-bordered') {
                    mkp.yieldUnescaped head
                    tbody {
                        tests.each {
                            mkp.yieldUnescaped it
                        }
                    }
                }
            }
            h1('Summary.')
            table('class':'overviewTable sort table-bordered') {
                thead {
                    tr('class':'columnHeadings fixed') {
                        th('SUMMARY')
                        th('Duration')
                        th('Prority')
                        th('Passed')
                        th('Skipped')
                        th('Failed')
                        th('Bug')
                        th('Closed Bug')
                        th('Content/Data/Env Problems')
                        th('System Issue')
                        th('Pass Rate')
                    }
                }
                tbody {
                    tr('class':'test goTo_link') {
                        td('Total count of tests: ' + summary['total'])
                        td('-')
                        td('-')
                        td(summary['success'])
                        td(summary['skipped'])
                        td(summary['failed'])
                        td(summary['bug'])
                        td(summary['closedBug'])
                        td(summary['problems'])
                        td(summary['system'])
                        td(summary['percent'] + '%')
                    }
                }
            }
        }
    }

    writeToFile('testAnalysisResult.html', sw.toString())
}

def toHtml(nodes) {
    def htmlString = ''
    nodes.each { htmlString += groovy.xml.XmlUtil.serialize(it)
            .replace('<?xml version="1.0" encoding="UTF-8"?>','')
            .replace(' xmlns="http://www.w3.org/1999/xhtml"', '')
    }
    return htmlString
}

sortByPriorities = {record1, record2 ->
    PRIORITIES[record1.td[2].text().trim()] <=> PRIORITIES[record2.td[2].text().trim()]
}

def getFailedTestCategory(builds) {
    def externalRootUrl = Jenkins.instance.getRootUrl().replace(':8080', '.corp.ad.ctc:8080')
    def categoryTests = [:]
    builds.each { build ->
        def path = build.getRootDir().toString() + '/htmlreports/HTML_Report/Full_Report/html/overview.html'
        def src = new File(path),
            page = slurper.parseText(src.getText('UTF-8').replace('&','&amp;'))


        def allHeads = page.'**'.findAll { it.name() == 'thead' && it.tr.@class.toString().contains('columnHeadings') }

        for (i = 0; i < allHeads.size(); i++) {
            def hasFailedTests = allHeads[i].parent().'**'.find {
                it.name() == 'td' && it.@class.toString().trim() == 'passRate suite' && it.text().trim() != '100.00%'
            }
            if (!hasFailedTests) allHeads.remove(i)
        }

        allHeads.each { head ->
            def failNodes = head.parent().tbody.children().findAll { it.td[10].text().trim() != '100.00%'}
            def failsArray = []
            failNodes.each {failsArray.push(it)} // failNodes has type NodeChildren and cant be sorted
            def sortedFails = failsArray.sort(sortByPriorities)

            def htmlFails = sortedFails.collect {
                def html = toHtml(it)
                def link = (html =~ /(?<=href=")(.*)(?=">)/)[0][1]
                html.replace(link, externalRootUrl + build.getUrl() + 'HTML_Report/Full_Report/html/' + link)
            }
            categoryTests[toHtml(head)] = htmlFails
        }
    }

    return categoryTests
}

def getNewFails(lastRun, prevRun) {
    def newFails = [:]
    lastRun.each { head, tests ->
        catFails = []
        tests.each { newFail ->
            def name = (newFail =~ /(?<=>)(.*)(?=<\/a>)/)[0][1]
            def inPrev = prevRun[head].find {it.contains(name)}
            if (!inPrev) catFails.push(newFail)
        }
        if (!catFails?.empty) {
            newFails[head] = catFails
        }
    }
    return newFails
}

def calculateTotalPassRate(paths) {
    def totalTests = 0,
        succTests = 0,
        failtests = 0,
        percent
    paths.each { path ->
        def src = new File(path),
            page = slurper.parseText(src.getText('UTF-8').replace('&','&amp;'))
        def totalNodes = page.'**'.findAll { it.@class == 'totalLabel'}
        def succNodes = page.'**'.findAll { it.@class == 'passed number' && it.parent().@class == 'suite'}
        totalNodes.each { totalTests += it.text().trim().minus('Total count of tests - ').toInteger() }
        succNodes.each { succTests += it.text().trim().toInteger() }
    }
    failTests = totalTests - succTests
    percent = (failTests * 100) / totalTests

    return new DecimalFormat("#.##").format(percent)
}

def calculateSummary(paths) {
    def summary = [total: 0, success: 0, skipped: 0, failed: 0, bug: 0, closedBug: 0, problems: 0, system: 0, percent: 0]
    def findNodes = {page, clazz -> page.'**'.findAll { it.@class == clazz && it.parent().@class == 'suite'}}
    def trimNum = {node -> node.text().trim().toInteger() }
    paths.each { path ->
        def src = new File(path),
            page = slurper.parseText(src.getText('UTF-8').replace('&','&amp;'))
        page.'**'.findAll { it.@class == 'totalLabel'}.each { summary['total'] += it.text().trim().minus('Total count of tests - ').toInteger() }
        findNodes(page, 'passed number').each { summary['success'] += trimNum(it) }
        findNodes(page, 'skipped number').each { summary['skipped'] += trimNum(it) }
        findNodes(page, 'failed number').each { summary['failed'] += trimNum(it) }
        findNodes(page, 'bug number').each { summary['bug'] += trimNum(it) }
        findNodes(page, 'closedBug number').each { summary['closedBug'] += trimNum(it) }
        findNodes(page, 'contentProblem number').each { summary['problems'] += trimNum(it) }
        findNodes(page, 'envIssue number').each { summary['system'] += trimNum(it) }
    }
    allFails = summary['total'] - summary['success']
    summary['percent'] = new DecimalFormat("#.##").format((summary['success'] * 100) / summary['total'])

    return summary
}

def environment = build.buildVariableResolver.resolve('ENVIRONMENT')
def buildType = build.buildVariableResolver.resolve('JOB_TYPE')

def buildFullName = '['+environment+']  ' + BUILD_NAMES[buildType]

def jobItems = getJobItems()

def lastRunBuilds = getJobLastBuilds(jobItems, buildFullName)
def prevRunBuilds = getJobPrevBuilds(lastRunBuilds)

def lastRunPaths = convertToPaths(lastRunBuilds)

slurper = new XmlSlurper(false, false)
slurper.setFeature('http://apache.org/xml/features/nonvalidating/load-external-dtd', false)

def lastRunFailedTestRecords = getFailedTestCategory(lastRunBuilds)
def prevRunFailedTestRecords = getFailedTestCategory(prevRunBuilds)

def failsPercent = calculateTotalPassRate(lastRunPaths)

def summary = calculateSummary(lastRunPaths)

def newFailedTestRecords = getNewFails(lastRunFailedTestRecords, prevRunFailedTestRecords)

buildDocument(lastRunFailedTestRecords, newFailedTestRecords, failsPercent, summary)