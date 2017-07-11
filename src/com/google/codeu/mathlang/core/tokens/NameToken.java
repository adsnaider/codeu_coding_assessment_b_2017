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

package com.google.codeu.mathlang.core.tokens;

// NAME TOKEN
//
// This class represents a UTF-8 string that contains no whitespaces
// and starts with an alphabetical character.
public final class NameToken implements Token {

  public final String value;

  public NameToken(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("NAME(%s)", value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof NameToken && equals(this, (NameToken) other);
  }

  private static boolean equals(NameToken a, NameToken b) {
    return a == b || (a != null && b!= null && a.value.equals(b.value));
  }
}
