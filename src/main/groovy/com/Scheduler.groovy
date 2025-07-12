package com

import org.quartz.CronScheduleBuilder
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobExecutionContext
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

class Scheduler {
    static void schedule(String cronExpression, Closure task){
        def scheduler = StdSchedulerFactory.defaultScheduler

        def job = JobBuilder.newJob(TaskJob)
            .withIdentity('fileJob', 'group1')
            .build()

        job.jobDataMap.put('task', task)

        def trigger = TriggerBuilder.newTrigger()
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build()

        scheduler.scheduleJob(job, trigger)
        scheduler.start()
        println "Scheduler started with cron: $cronExpression"
    }

    static class TaskJob implements Job{
        void execute(JobExecutionContext context){
            def task = context.jobDetail.jobDataMap.get('task') as Closure
            task.call()
        }
    }
}
