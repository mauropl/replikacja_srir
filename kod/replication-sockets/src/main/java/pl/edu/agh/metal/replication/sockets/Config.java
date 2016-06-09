package pl.edu.agh.metal.replication.sockets;

import java.util.regex.Pattern;

public class Config {

    /**
     * Port do którego łączą się klienci
     */
    public static final int PRIMARY_CLIENT_PORT = 8000;

    /**
     * Port do którego łączą się backupy
     */
    public static final int PRIMARY_BACKUP_PORT = 10000;
    public static final String PRIMARY_HOST = "127.0.0.1";

    public static final Pattern EXIT_PATTERN = Pattern.compile("^exit$");
    public static final Pattern WRITE_PATTERN = Pattern.compile("^write (\\d\\s*){2}$");
    public static final Pattern SYNCHRONIZE_PATTERN = Pattern.compile("^synchronize (\\d\\s*){10}$");
    public static final Pattern READ_PATTERN = Pattern.compile("^read \\d{1}$");

}
