package com.epam

import java.util.zip.*


static def create_archive(env) {
    def TIMESTAMP = new java.text.SimpleDateFormat('yyyyMMddHHmmss').format(new Date())
    def jobBaseName = "${env.JOB_NAME}".split('/')
    def projectName = "${jobBaseName[0]}"
    String zipFileName = "${env.WORKSPACE}/${projectName}_${TIMESTAMP}.zip"
    String inputDir = "${env.WORKSPACE}"

    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))
    new File(inputDir).eachFile() { file ->
        //check if file
        if (file.isFile()){
            zipFile.putNextEntry(new ZipEntry(file.name))
            def buffer = new byte[file.size()]
            file.withInputStream {
                zipFile.write(buffer, 0, it.read(buffer))
            }
            zipFile.closeEntry()
        }
    }
    zipFile.close()
}