package com.jiucheng.service;

import org.springframework.stereotype.Service;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.tran.DataStream;
import org.frameworkset.tran.ExportResultHandler;
import org.frameworkset.tran.db.input.es.DB2ESImportBuilder;
import org.frameworkset.tran.metrics.TaskMetrics;
import org.frameworkset.tran.schedule.ImportIncreamentConfig;
import org.frameworkset.tran.task.TaskCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DataToEsService
 *
 * @auther qiaoba
 * @date 2020/10/10 0010 9:36
 **/
@Service
@SuppressWarnings("all")
public class DataToEsService {
    private Logger logger = LoggerFactory.getLogger(DataToEsService.class);
    @Autowired
    private BBossESStarter bbossESStarter;
    private DB2ESImportBuilder db2ESImportBuilder;
    private DataStream dataStream;
    public  String startJob(){
        if (db2ESImportBuilder == null) {
            synchronized (this) {
                if (db2ESImportBuilder == null) {
                    DB2ESImportBuilder importBuilder = DB2ESImportBuilder.newInstance();
                    importBuilder.setDbName("bboss")
                            .setDbDriver("com.mysql.jdbc.Driver") //数据库驱动程序，必须导入相关数据库的驱动jar包
                            //mysql stream机制二  jdbcFetchSize为Integer.MIN_VALUE即可，url中不需要设置useCursorFetch=true参数，这里我们使用机制二
                            .setDbUrl("jdbc:mysql://localhost:3306/my_pratice?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                            .setJdbcFetchSize(Integer.MIN_VALUE)//启用mysql stream机制二,设置jdbcfetchsize大小为Integer.MIN_VALUE
                            .setDbUser("root")
                            .setDbPassword("123456")
                            .setValidateSQL("select 1")
                            .setUsePool(false);//是否使用连接池
                    //指定导入数据的sql语句，必填项，可以设置自己的提取逻辑，
                    //importBuilder.setSql("select * from poems where update_time > #[update_time]");
                    importBuilder.setSql("select * from student");
                    /**
                     * es相关配置
                     */
                    importBuilder.setTargetElasticsearch("default");
                    importBuilder
                            .setIndex("student") //必填项
                            .setIndexType("docs") //es 7以后的版本不需要设置indexType，es7以前的版本必需设置indexType
                            .setRefreshOption("refresh")//可选项，null表示不实时刷新，importBuilder.setRefreshOption("refresh");表示实时刷新
                            .setUseJavaName(false) //可选项,将数据库字段名称转换为java驼峰规范的名称，true转换，false不转换，默认false，例如:doc_id -> docId
                            .setUseLowcase(false)  //可选项，true 列名称转小写，false列名称不转换小写，默认false，只要在UseJavaName为false的情况下，配置才起作用
                            .setPrintTaskLog(true) //可选项，true 打印任务执行日志（耗时，处理记录数） false 不打印，默认值false
                            .setBatchSize(10);  //可选项,批量导入es的记录数，默认为-1，逐条处理，> 0时批量处理

                    //定时任务配置，
                    importBuilder.setFixedRate(false)//参考jdk timer task文档对fixedRate的说明
                            //.setScheduleDate(date) //指定任务开始执行时间：日期
                            .setDeyLay(1000L) // 任务延迟执行deylay毫秒后执行
                            .setPeriod(5000L); //每隔period毫秒执行，如果不设置，只执行一次

                    //增量配置
                    //importBuilder.setNumberLastValueColumn("log_id");//手动指定数字增量查询字段，默认采用上面设置的sql语句中的增量变量名称作为增量查询字段的名称，指定以后就用指定的字段
                    //importBuilder.setDateLastValueColumn("log_id");//手动指定日期增量查询字段，默认采用上面设置的sql语句中的增量变量名称作为增量查询字段的名称，指定以后就用指定的字段
                    importBuilder.setFromFirst(true);//任务重启时，重新开始采集数据，true 重新开始，false不重新开始，适合于每次全量导入数据的情况，如果是全量导入，可以先删除原来的索引数据
                    importBuilder.setLastValueStorePath("save_field");//记录上次采集的增量字段值的文件路径，作为下次增量（或者重启后）采集数据的起点，不同的任务这个路径要不一样
                    //importBuilder.setLastValueStoreTableName("logs");//记录上次采集的增量字段值的表，可以不指定，采用默认表名increament_tab
                    importBuilder.setLastValueType(ImportIncreamentConfig.TIMESTAMP_TYPE);//如果没有指定增量查询字段名称，则需要指定字段类型：ImportIncreamentConfig.NUMBER_TYPE 数字类型
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = format.parse("2000-01-01");
                        importBuilder.setLastValue(date);//增量起始值配置
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 或者ImportIncreamentConfig.TIMESTAMP_TYPE 日期类型
                    /**
                     * 一次、作业创建一个内置的线程池，实现多线程并行数据导入elasticsearch功能，作业完毕后关闭线程池
                     */
                    importBuilder.setParallel(true);//设置为多线程并行批量导入,false串行
                    importBuilder.setQueue(10);//设置批量导入线程池等待队列长度
                    importBuilder.setThreadCount(50);//设置批量导入线程池工作线程数量
                    importBuilder.setContinueOnError(true);//任务出现异常，是否继续执行作业：true（默认值）继续执行 false 中断作业执行
                    importBuilder.setAsyn(false);//true 异步方式执行，不等待所有导入作业任务结束，方法快速返回；false（默认值） 同步方式执行，等待所有导入作业任务结束，所有作业结束后方法才返回
                    importBuilder.setEsIdField("id");//设置文档主键，不设置，则自动产生文档id
                    // importBuilder.setDebugResponse(false);//设置是否将每次处理的reponse打印到日志文件中，默认false，不打印响应报文将大大提升性能，只有在调试需要的时候才打开，log日志级别同时要设置为INFO
                    //importBuilder.setDiscardBulkResponse(true);//设置是否需要批量处理的响应报文，不需要设置为false，true为需要，默认true，如果不需要响应报文将大大提升处理速度
                    importBuilder.setDebugResponse(false);//设置是否将每次处理的reponse打印到日志文件中，默认false
                    importBuilder.setDiscardBulkResponse(false);//设置是否需要批量处理的响应报文，不需要设置为false，true为需要，默认false
                    /**
                     importBuilder.setEsIdGenerator(new EsIdGenerator() {
                     //如果指定EsIdGenerator，则根据下面的方法生成文档id，
                     // 否则根据setEsIdField方法设置的字段值作为文档id，
                     // 如果默认没有配置EsIdField和如果指定EsIdGenerator，则由es自动生成文档id

                     @Override public Object genId(Context context) throws Exception {
                     return SimpleStringUtil.getUUID();//返回null，则由es自动生成文档id
                     }
                     });
                     */
                    importBuilder.setExportResultHandler(new ExportResultHandler<String, String>() {
                        @Override
                        public void success(TaskCommand<String, String> taskCommand, String result) {
                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                            logger.info(taskMetrics.toString());
                            logger.info(result);
                        }

                        @Override
                        public void error(TaskCommand<String, String> taskCommand, String result) {
                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                            logger.info(taskMetrics.toString());
                            logger.info(result);
                        }

                        @Override
                        public void exception(TaskCommand<String, String> taskCommand, Exception exception) {
                            TaskMetrics taskMetrics = taskCommand.getTaskMetrics();
                            logger.info(taskMetrics.toString());
                        }

                        @Override
                        public int getMaxRetry() {
                            return 0;
                        }
                    });
                    /**
                     * 执行数据库表数据导入es操作
                     */
                    DataStream dataStream = importBuilder.builder();
                    dataStream.execute();//执行导入操作
                    db2ESImportBuilder = importBuilder;
                    this.dataStream = dataStream;
                    return "db2ESImport job started.";
                }
                else{
                    return "db2ESImport job has started.";
                }
            }
        }
        else{
            return "db2ESImport job has started.";
        }

    }

    public String stopJob(){
        if(dataStream != null) {
            synchronized (this) {
                if (dataStream != null) {
                    dataStream.destroy();
                    dataStream = null;
                    db2ESImportBuilder = null;
                    return "db2ESImport job stopped.";
                } else {
                    return "db2ESImport job has stopped.";
                }
            }
        }
        else {
            return "db2ESImport job has stopped.";
        }
    }
}
