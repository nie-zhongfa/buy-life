package org.buy.life;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @Author kavin
 * @Date 2023/2/7
 */
public class MybatisGenerator {
    /**
     * 表名逗号分割
     */
//    private static final String tables = "buy_user,buy_sku,buy_cart,buy_order,buy_order_detail";
    private static final String tables = "buy_order_change_log";

    public static void main(String[] args) {
        // 创建generator对象
        AutoGenerator mpg = new AutoGenerator();
        // 数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://101.132.155.114:3306/buy_life?useSSL=false&charset=utf8mb4&allowMultiQueries=true&serverTimezone=GMT%2b8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        //com.mysql.jdbc.Driver
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("xiguatianbutian");
        mpg.setDataSource(dataSourceConfig);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("MrWu");
        // 覆盖
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);

        gc.setOpen(false);
        gc.setEntityName("%sEntity");
        mpg.setGlobalConfig(gc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("org.buy.life");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");

        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 表中_ 变成驼峰命名配置
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动+lombok注解
        strategy.setEntityLombokModel(true);
        // 只生成单张部门表的代码
        strategy.setInclude(tables.split(","));
        // 生成rest 风格代码
        strategy.setRestControllerStyle(false);
        mpg.setStrategy(strategy);

        mpg.execute();
    }

}
