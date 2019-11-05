package com.fileexchangr.demo.configs;

public class StringConstants {
    public static final String findUser = "select username, password, true from usr where username=?";

    public static final String findUserRole = "select u.username, ur.roles " +
            "from usr u inner join user_role ur on u.id = ur.user_id " +
            "where u.username=?";

    public static final String downloadLimit = "Download limit exceeded";

    public static final String warning = "warning";

    public static final String attachmentFilename = "attachment; filename=\"";

    public static final String message = "message";

    public static final String userExists = "User already exists";

    public static final String userNotFound = "User not found!";

    public static final String exception = "exception";
}
