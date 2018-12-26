package integration

enum Path {
    LOGIN('/login'),
    POST_SCORES('/levels/%d/users/%s'),
    GET_SCORES('/levels/%d/scores')

    private final String message

    Path(message) {
        this.message = message
    }

    def value() {
        message
    }

    def value(Object... args) {
        sprintf message, args
    }
}
