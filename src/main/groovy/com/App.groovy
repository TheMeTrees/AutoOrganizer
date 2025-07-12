package com

import groovy.cli.commons.CliBuilder

class App {
    static void main(String[] args) {

        def cli = new CliBuilder(usage: 'file-organizer [options]')
        cli.d(longOpt: 'directory', args: 1, required: true, 'Source directory')
        cli.n(longOpt: 'dry-run', 'Enable dry run (no file moves)')
        cli.s(longOpt: 'schedule', args: 1, 'Cron expression to schedule organization')

        def options = cli.parse(args)
        if (!options) return

        def dir = new File(options.d)
        def dryRun = options.n

        if (options.s) {
            Scheduler.schedule(options.s, {
                FileOrganizer.organize(dir, dryRun)
            })
        } else {
            FileOrganizer.organize(dir, dryRun)
        }
    }
}
