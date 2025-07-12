package com

import spock.lang.Specification

class FileOrganizerSpec extends Specification {

    def "dry-run does not move files"() {
        given:
        // Create a temporary directory and a dummy file
        def tempDir = new File("build/test-dir")
        tempDir.mkdirs()
        def testFile = new File(tempDir, "test.pdf")
        testFile.text = "dummy"

        when:
        // Run the organizer in dry-run mode
        FileOrganizer.organize(tempDir, true)

        then:
        // The file should still be in its original location
        testFile.exists()

        cleanup:
        // Clean up after test
        testFile.delete()
        tempDir.deleteDir()
    }
}
