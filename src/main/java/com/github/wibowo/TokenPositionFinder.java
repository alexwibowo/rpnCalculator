package com.github.wibowo;

public final class TokenPositionFinder {

    private TokenPositionFinder() {}

    /**
     * Utility class to find the position of token with the given index, in the command line.
     * A command line consists of one or more tokens.
     * e.g.:
     * command line "5 2" consists of two tokens, "5" and "2"
     * command line "5 2 +" consists of three tokens, "5", "2" and "+"
     *
     * Given command line "5 2 +", and tokenIndex of 2,  this method will find the index of "+" operation in the
     * command line string.
     * For "5 2 +", this method will return 5 (return value starts from 1)
     *
     * @param commandLine command line
     * @param tokenIndex 0 based index of the token in the list of parsed tokens
     * @return string index of token in the original line, 1-based.
     */
    public static int findTokenPositionInOriginalLine(final String commandLine,
                                                       final int tokenIndex) {
        final String[] tokens = commandLine.trim().split("\\s+");
        if (tokenIndex > tokens.length) {
            throw new IllegalArgumentException(
                    String.format(
                            "Unable to find position of operation with index [%d] in line [%s]. This might be a programming issue.",
                            tokenIndex, commandLine
                    ));
        }

        String remainingLine = commandLine.trim();

        for (int i = 0; i < tokenIndex; i++) {
            String currentToken = tokens[i];
            if (currentToken.equals("*")) {
                currentToken = "\\*";
            }
            if (currentToken.equals("+")) {
                currentToken = "\\+";

            }
            if (currentToken.equals("/")) {
                currentToken = "\\/";
            }
            remainingLine = remainingLine.replaceFirst(currentToken, "");
            remainingLine = remainingLine.replaceAll("^\\s+", "");
        }
        return commandLine.indexOf(remainingLine) + 1;
    }
}
