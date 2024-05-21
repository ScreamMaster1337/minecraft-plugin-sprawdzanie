package pl.m4code.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;

@Getter
public class Configuration extends OkaeriConfig {
    private String host = "localhost";
    private int port = 3306;
    private String database = "database";
    private String username = "username";
    private String password = "password";
    private boolean ssl = false;

    private boolean strikeonDeath = true;
}