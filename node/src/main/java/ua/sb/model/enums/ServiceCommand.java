package ua.sb.model.enums;

public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String value;

    ServiceCommand(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommand fromValue(String value) {
        for (ServiceCommand v: ServiceCommand.values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        return null;
    }
}
