package com

import org.apache.logging.log4j.LogManager


class FileOrganizer {
    private static final log = LogManager.getLogger(FileOrganizer)

    static final Map<String, List<String>> typeMap = [
            images : ['jpg', 'jpeg', 'png', 'gif'],
            audio : ['mp3', 'wav'],
            docs : ['pdf', 'docx', 'doc', 'txt'],
            videos : ['mp4', 'mov', 'avi'],
            archives : ['zip', 'rar', 'tar', 'gz']
    ]

    static void organize(File dir, boolean dryRun = false){
        if(!dir.exists() || !dir.isDirectory()){
            log.error("Invalid directory: ${dir.absolutePath}")
            return
        }

        dir.eachFile {file ->
            if (file.isFile()) {
               def extract = file.name.tokenize('.').last().toLowerCase()
                def folder = typeMap.find{k, v -> extract in v}?.key ?: 'others'
                def targetDir = new File(dir, folder)

                if (!targetDir.exists() && !dryRun){
                    targetDir.mkdir()
                }

                if(dryRun){
                    log.info("[DRY-RUN] Would move ${file.name} to ${folder}/")
                } else {
                    def success = file.renameTo(new File(targetDir, file.name))
                    if (success) {
                        log.info("Moved ${file.name} to ${folder}/")
                    } else {
                        log.warn("Failed to move ${file.name}")
                    }
                }
            }
        }
    }
}
