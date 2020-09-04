SELECT min(us_min_used)/1000000  as minUsed, max(us_max_used)/1000000  as maxUsed, avg(us_total/(tm_success+tm_failed))/1000000 as avgUsed,
       min(us_min_match)/1000000 as minMatch,max(us_max_match)/1000000 as maxMatch,avg(us_match/(tm_success+tm_failed))/1000000 as avgMatch, 
       sum(tm_success) as tmSuccess, sum(tm_failed) as tmFailed, sum(tm_found) as tmFound, sum(tm_not_found) as tmNotFound, sum(tm_multi_found) as tmMultiFound,
       sum(tm_success+tm_failed) as matchTimes, sum(tm_success+tm_failed)/TimeStampDiff(SECOND,dt_start, max(dt_end)) as caps, dt_start as dtStart, max(dt_end) as dtEnd
       FROM iotequ3.dev_svas_test group by dt_start