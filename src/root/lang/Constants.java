/*
 * Copyright 2006-2016 Edward Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.lang;

import java.nio.charset.Charset;
import java.util.TimeZone;

import root.annotation.Todo;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo("Move constants in Safe.java to here")
public interface Constants {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	String EMPTY_STRING = "";

	String NULL_STRING = "null";

	String EMPTY_ARRAY_STRING = "[]";

	TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

	char[] emptyCharArray = {};

	char[] emptyArray = { '[', ']' };

	char[] nullCharArray = { 'n', 'u', 'l', 'l' };

	char[] trueCharArray = { 't', 'r', 'u', 'e' };

	char[] falseCharArray = { 'f', 'a', 'l', 's', 'e' };

	char[] notANumber = { 'N', 'a', 'N' };

	char[] posInfinity = { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };

	char[] negInfinity = { '-', 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };

	char[] posZero = { '0', '.', '0' };

	char[] negZero = { '-', '0', '.', '0' };

	char[] intMinValue = { '-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8' };

	char[] colonSpace = { ':', ' ' };

	char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	char[] digitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };

	char[] digitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2',
			'2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5',
			'5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
			'7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', };

	char[][] dayOfWeek = { { 'T', 'h', 'u' }, { 'F', 'r', 'i' }, { 'S', 'a', 't' }, { 'S', 'u', 'n' }, { 'M', 'o', 'n' }, { 'T', 'u', 'e' },
			{ 'W', 'e', 'd' } };

	char[][] month = { { 'J', 'a', 'n' }, { 'F', 'e', 'b' }, { 'M', 'a', 'r' }, { 'A', 'p', 'r' }, { 'M', 'a', 'y' }, { 'J', 'u', 'n' },
			{ 'J', 'u', 'l' }, { 'A', 'u', 'g' }, { 'S', 'e', 'p' }, { 'O', 'c', 't' }, { 'N', 'o', 'v' }, { 'D', 'e', 'c' } };

} // End Constants
