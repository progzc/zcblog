package com.progzc.blog.common.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 代码生成器
 * @Author zhaochao
 * @Date 2020/10/24 17:43
 * @Email zcprog@foxmail.com
 * @Version V1.0
 */
public class CodeGeneratorUtils {

    public static String projectPath = System.getProperty("user.dir");
    /**
     * 根据表名自动生成代码
     * @param tableName
     * @param moduleName
     * @param category
     */
    public static void codeGenerator(String tableName, String moduleName, String category){
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "\\zcblog-backend\\zcblog-core\\src\\main\\java");
        gc.setAuthor("zhaochao");
        gc.setOpen(false); // 不打开输出目录
        gc.setBaseResultMap(true); // 开启BaseResultMap
        gc.setBaseColumnList(true); // 开启baseColumnList
        gc.setSwagger2(true); // 实体属性添加Swagger2注解
        gc.setControllerName("%sController"); // controller命名方式为在末尾添加Controller
        gc.setServiceName("%sService"); // service命名方式为在末尾增加Service
        gc.setServiceImplName("%sServiceImpl"); // service实现类命名方式为在末尾添加ServiceImpl
        gc.setMapperName("%sMapper"); // mapper命名方式为在末尾增加Mapper
        gc.setIdType(IdType.AUTO); // 默认主键自增类型为数据库自增
        gc.setDateType(DateType.ONLY_DATE); // 设置日期格式
        gc.setFileOverride(false); // 不覆盖原来文件（否则会比较危险），也可在cfg.setFileCreate中进行自定义配置
        mpg.setGlobalConfig(gc); // 为代码生成器注入全局配置

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/zcblog?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc); // 添加数据源配置

        // 包配置（后面手动单独配置）
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.progzc.blog"); // 设置包名称
        pc.setModuleName(moduleName);  // 若设置后会生成com.progzc.blog/${moduleName}包
        pc.setEntity("entity" + "." + category); // 生成entity文件夹,设置后会生成com.progzc.blog/${moduleName}/entity/${category}包
        pc.setMapper("mapper" + "." + category); // 生成mapper文件夹,设置后会生成com.progzc.blog/${moduleName}/mapper/${category}包
        pc.setService("service" + "." + category); // 生成service文件夹,设置后会生成com.progzc.blog/${moduleName}/service/${category}包
        pc.setServiceImpl("service.impl" + "." + category); // 生成service.impl文件夹,设置后会生成com.progzc.blog/${moduleName}/service/impl/${category}包
        pc.setController("controller" + "." + category); // 生成controller文件夹,设置后会生成com.progzc.blog/${moduleName}/controller/${category}包
        pc.setXml(null); // *Mapper.xml使用自定义配置
        mpg.setPackageInfo(pc); // 添加包配置信息

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 当代码生成器自动生成好代码后，若后续不需再重新生成了，为了防止误操作，应修改为return false
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                return !new File(filePath).exists(); // 若文件存在，则不会重新生成
            }
        });

        // 以下可以自定义配置，但有个两个注意点：
        // 1. new FileOutConfig("/templates/mapper.xml.vm")中的模板文件一定要带后缀.vm，否则会报错。
        // 2. outputFile方法中返回的文件路径中的文件夹需要事先创建好，否则会报错。(这里我们对原程序进行改进)
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") { // 这里一定要带后缀.vm告知使用Velocity进行解析
            @Override
            public String outputFile(TableInfo tableInfo) {
                StringBuilder xmlfilePath = new StringBuilder();
                xmlfilePath.append(projectPath)
                           .append("\\zcblog-backend\\zcblog-core\\src\\main\\resources/mapper\\")
                           .append(category);
                File file = new File(xmlfilePath.toString());
                if(!file.exists() || !file.isDirectory()){
                    file.mkdirs();
                }
                String xmlfileName = tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return xmlfilePath + "\\" + xmlfileName;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplateEngine(new VelocityTemplateEngine()); // 设置使用Velocity模板引擎

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setController("templates/controller.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setXml(null); // *Mapper.xml使用自定义配置
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableName); // 设置表名
        strategy.setLogicDeleteFieldName("deleted"); // 逻辑删除
        TableFill create_time = new TableFill("create_time", FieldFill.INSERT); // 设置自动填充
        TableFill update_time = new TableFill("update_time", FieldFill.UPDATE); // 设置逻辑删除
        List<TableFill> fillList = new ArrayList<TableFill>();
        fillList.add(create_time);
        fillList.add(update_time);
        strategy.setTableFillList(fillList);
        strategy.setVersionFieldName("version"); // 乐观锁
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    // 自定义TableModule封装tableName、moduleName、category参数
    static class TableModule{
        private String tableName;
        private String moduleName;
        private String category;

        /**
         * 生成的文件：${outputDir}/${parent}/${moduleName}/${fileType}/${category}/文件名
         * @param tableName 表名
         * @param moduleName 模块名
         * @param category 分类
         */
        public TableModule(String tableName, String moduleName, String category){
            this.tableName = tableName;
            this.moduleName = moduleName;
            this.category = category;
        }
    }

    public static void main(String[] args) {
        List<TableModule> list = new ArrayList<TableModule>();
        list.add(new TableModule("article", null, "article"));
        list.add(new TableModule("gallery", null,  "gallery"));
        list.add(new TableModule("encrypt", null, "operation"));
        list.add(new TableModule("tag", null, "operation"));
        list.add(new TableModule("tag_link", null,  "operation"));
        list.add(new TableModule("log_like", null,  "log"));
        list.add(new TableModule("log_view", null,  "log"));
        list.add(new TableModule("oss_resource", null,  "oss"));
        list.add(new TableModule("sys_menu", null,  "sys"));
        list.add(new TableModule("sys_role", null,  "sys"));
        list.add(new TableModule("sys_role_menu", null,  "sys"));
        list.add(new TableModule("sys_user", null,  "sys"));
        list.add(new TableModule("sys_user_role", null,  "sys"));
        list.forEach( e -> codeGenerator(e.tableName, e.moduleName, e.category));
    }
}
