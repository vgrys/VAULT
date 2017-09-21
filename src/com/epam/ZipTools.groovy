package com.epam

import java.util.zip.*


static def bundle(env) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    String zipFilePath = "${env.WORKSPACE}/${projectName}_${TIMESTAMP}.zip"
    String sourceFolder = "${env.WORKSPACE}"

    (new AntBuilder()).zip(destfile: zipFilePath, basedir: sourceFolder)

//    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFilePath))
//    new File(inputDir).eachFile() { file ->
//        if (file.isFile()){
//            zipFile.putNextEntry(new ZipEntry(file.name))
//            def buffer = new byte[file.size()]
//            file.withInputStream {
//                zipFile.write(buffer, 0, it.read(buffer))
//            }
//            zipFile.closeEntry()
//        }
//    }
//    zipFile.close()
    return zipFilePath
}