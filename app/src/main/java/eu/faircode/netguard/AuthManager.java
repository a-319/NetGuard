package eu.faircode.netguard;

public class AuthManager {
    // 'volatile' ensures that changes to this variable are visible across all threads.
    private static volatile boolean authenticated = false;

    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static void setAuthenticated(boolean status) {
        authenticated = status;
    }
}