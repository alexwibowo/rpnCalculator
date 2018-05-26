package com.github.wibowo;

public final class CommandStringHelper {

    private CommandStringHelper() {}

    /**
     *
     * remainingLine = [1 2 3 * 5 + * * 6 5]
     * tokens: [1,2,3,*,5,+,*,*,6,5]
     * tokenIndex=7
     *
     * first iteration:
     * remainingLine = [2 3 * 5 + * * 6 5]
     *
     * so on until we left with
     * remainingLine = [* * 6 5]
     *
     * then: find "* * 6 5" in the original line
     *
     * @param line command line
     * @param tokenIndex 0 based index of the token in the list of parsed tokens
     * @return string index of token in the original line
     */
    public static int findTokenPositionInOriginalLine(final String line,
                                                       final int tokenIndex) {
        final String[] tokens = line.trim().split("\\s+");
        if (tokenIndex > tokens.length) {
            throw new IllegalArgumentException(
                    String.format(
                            "Unable to find position of operation with index [%d] in line [%s]. This might be a programming issue.",
                            tokenIndex, line
                    ));
        }

        String remainingLine = line.trim();

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
        return line.indexOf(remainingLine) + 1;
    }
}
