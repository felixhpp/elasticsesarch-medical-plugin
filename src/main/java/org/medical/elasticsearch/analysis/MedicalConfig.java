package org.medical.elasticsearch.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.medical.elasticsearch.plugin.MedicalPlugin;

import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;

public class MedicalConfig {
    private Environment environment;
    private Settings settings;
    //ner 分词服务url地址
    private String nerServer;
    // config路径
    private Path conf_dir;
    private Properties props;

    public static ESLogger logger = Loggers.getLogger("medical-analyzer");

    // 配置文件名称
    private final static  String FILE_NAME = "MedicalAnalyzer.cfg.xml";
    private final static  String ANALYZER_SERVER = "analyzer_server";

    public MedicalConfig(){
        this.props = new Properties();
    }
    public MedicalConfig(Environment env, Settings settings) {
        this.environment = env;
        this.settings=settings;

        this.props = new Properties();

        Init();
    }

    /**
     * 初始化
     */
    public void Init() {
        this.conf_dir = this.environment.configFile().resolve(MedicalPlugin.PLUGIN_NAME);
        Path configFile = conf_dir.resolve(FILE_NAME);
        InputStream input = null;
        try {
            logger.info("try load config from {}", configFile);
            input = new FileInputStream(configFile.toFile());
        } catch (FileNotFoundException e) {
            conf_dir = this.getConfigInPluginDir();
            configFile = conf_dir.resolve(FILE_NAME);
            try {
                logger.info("try load config from {}", configFile);
                input = new FileInputStream(configFile.toFile());
            } catch (FileNotFoundException ex) {
                logger.error("medical-analyzer", e);
            }
        }

        if (input != null) {
            try {
                props.loadFromXML(input);
            } catch (InvalidPropertiesFormatException e) {
                logger.error("medical-analyzer", e);
            } catch (IOException e) {
                logger.error("medical-analyzer", e);
            }
        }

        String analyzerServer = getProperty(ANALYZER_SERVER);
        if (analyzerServer != null) {
            this.nerServer = analyzerServer;
            logger.info("medical-analyser url :{}", analyzerServer);
        }
    }

    /**
     * 获取配置文件目录
     * @return
     */
    public Path getConfigInPluginDir() {
        return PathUtils
                .get(new File(MedicalPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                        .getParent(), "config")
                .toAbsolutePath();
    }

    /**
     * 获取配置文件指定key的值
     * @param key key name
     * @return
     */
    public String getProperty(String key){
        if(props!=null){
            return props.getProperty(key);
        }
        return null;
    }

    /**
     * 获取ner api
     * @return
     */
    public String getNerServer() {
        return this.nerServer;
    }

    public void setNerServer(String url) {
        this.nerServer = url;
    }
}
