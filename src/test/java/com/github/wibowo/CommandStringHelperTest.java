package com.github.wibowo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandStringHelperTest {

    @Test
    void finding_token_at_start() {
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1 2 3", 0);
        assertThat(tokenPositionInOriginalLine).isEqualTo(1);
    }

    @Test
    void finding_token_at_start_where_there_is_leading_whitespace() {
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("    1 2 3", 0);
        assertThat(tokenPositionInOriginalLine).isEqualTo(5);
    }

    @Test
    void finding_token_index_on_line_with_single_whitespace_separators() {
        // getting 3rd "*"
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1 2 3 * 5 + * * 6 5", 7);
        assertThat(tokenPositionInOriginalLine).isEqualTo(15);
    }

    @Test
    void finding_token_index_on_line_with_multi_whitespace_separators() {
        // getting 3rd "*"
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1    2   3   *  5 + * * 6 5", 7);
        assertThat(tokenPositionInOriginalLine).isEqualTo(23);
    }

    @Test
    void finding_token_index_on_line_containing_division_token() {
        // getting 2nd "*"
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1    2   4   /  5 + * * 6 5", 7);
        assertThat(tokenPositionInOriginalLine).isEqualTo(23);
    }

    @Test
    void finding_token_index_on_line_containing_sqrt_token() {
        // getting "6"
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1 4 sqrt 5 6", 4);
        assertThat(tokenPositionInOriginalLine).isEqualTo(12);
    }

    @Test
    void finding_token_index_on_line_containing_minus_token() {
        // getting "6"
        final int tokenPositionInOriginalLine = CommandStringHelper.findTokenPositionInOriginalLine("1 4 - 5 6", 4);
        assertThat(tokenPositionInOriginalLine).isEqualTo(9);
    }

    @Test
    void finding_token_beyond_available_tokens() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> CommandStringHelper.findTokenPositionInOriginalLine("1 2 3", 4)
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to find position of operation with index [4] in line [1 2 3]. This might be a programming issue.");
    }

}