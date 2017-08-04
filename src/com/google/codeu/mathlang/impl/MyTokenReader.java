// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private String source;
  private int current = 0;
  private List<String> splits;

  public MyTokenReader(String source) throws IOException {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
    splits = split(source);
  }

  @Override
  public Token next() throws IOException {

    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.

    if (hasNext()) {
      String next = splits.get(current++);
      if (isString(next)) {
        return new StringToken(next.substring(1, next.length() - 1));
      }
      if (isNumber(next)) {
        return new NumberToken(Double.parseDouble(next));
      }
      if (isSymbol(next)) {
        return new SymbolToken(next.charAt(0));
      }
      if (isName(next)) {
        return new NameToken(next);
      }
      throw new IOException("Invalid input.");
    }

    return null;
  }

  private static boolean isString(String value) {
    return (value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"');
  }

  private static boolean isNumber(String value) {
    boolean isDecimal = false;
    for (char x : value.toCharArray()) {
      if (Character.isDigit(x)) continue;
      if (x == '.') {
        if (isDecimal) {
          return false;
        }
        isDecimal = true;
        continue;
      }
      return false;
    }
    return true;
  }

  private static boolean isName(String value) {
    return Character.isLetter(value.charAt(0));
  }

  private static boolean isSymbol(String value) {
    if (value.length() == 1) {
      return isSymbol(value.charAt(0));
    }
    return false;
  }

  private static boolean isSymbol(char value) {
    return (value == '-'
        || value == '+'
        || value == '='
        || value == ';'
        || value == '*'
        || value == '/');
  }

  // Core of the problem. This function figures out how to split the function source to get
  // every Token by itself. It also throws IOException in some cases if the input is incorrect.
  private static List<String> split(final String source) throws IOException {
    List<String> result = new ArrayList<>();
    int last_split = -1;
    boolean opened_quotes = false;
    for (int i = 0; i < source.length(); i++) {
      char value = source.charAt(i);
      if (i == source.length() - 1 || source.charAt(i + 1) == '\n') {
        if (value != ';') {
          throw new IOException("Invalid end of statement");
        }
        String split = source.substring(last_split + 1, i);
        last_split = i;
        if (split.length() != 0) {
          result.add(split);
        }
        result.add(";");
      }

      if (opened_quotes) {
        if (value == '\"') {
          opened_quotes = false;
          String split = source.substring(last_split + 1, i + 1);
          last_split = i;
          if (split.length() == 0) continue;
          result.add(split);
        }
        if (value == '\n') {
          throw new IOException("Invalid input.");
        }
      } else {
        if (value == '\"') {
          opened_quotes = true;
        } else if (Character.isWhitespace(value)) {
          String split = source.substring(last_split + 1, i);
          last_split = i;
          if (split.length() == 0) continue;
          result.add(split);
        } else if (isSymbol(value) && value != ';') {
          String split = source.substring(last_split + 1, i);
          last_split = i;
          if (split.length() != 0) {
            result.add(split);
          }
          result.add(Character.toString(value));
        }
      }
    }
    if (opened_quotes) {
      throw new IOException("Invalid input.");
    }

    return result;
  }

  private boolean hasNext() {
    if (current >= splits.size()) {
      return false;
    }
    return true;
  }

  public static void main(String[] args) throws IOException {
    MyTokenReader reader = new MyTokenReader(lines("let x = 5;", "let y = x + -3;", "print y;"));
    while (reader.hasNext()) {
      System.out.println(reader.next());
    }
  }

  private static String lines(String... lines) {

    final StringBuilder builder = new StringBuilder();

    for (final String line : lines) {
      builder.append(line).append("\n");
    }

    // Remove the trailing new line before returning.
    return builder.substring(0, Math.max(0, builder.length() - 1));
  }
}
