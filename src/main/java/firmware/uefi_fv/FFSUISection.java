/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package firmware.uefi_fv;

import ghidra.app.util.bin.BinaryReader;
import ghidra.formats.gfilesystem.GFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parser for FFS UI sections, which have the following specific field:
 *
 *   UEFI FFS UI Section Header
 *   +-----------+------+----------------------------------+
 *   | Type      | Size | Description                      |
 *   +-----------+------+----------------------------------+
 *   | wchar_t[] |  var | UI Section Text (Unicode string) |
 *   +-----------+------+----------------------------------+
 *
 * This header follows the common section header. See FFSSection for additional information.
 */
public class FFSUISection extends FFSSection {
	// Original header fields
	private String uiText;

	/**
	 * Constructs a FFSUISection from a specified BinaryReader.
	 *
	 * @param reader the specified BinaryReader
	 */
	public FFSUISection(BinaryReader reader) throws IOException {
		super(reader);

		// Read the UI section text.
		uiText = reader.readNextUnicodeString((int) length() / 2);
	}

	/**
	 * Constructs a FFSUISection from a specified BinaryReader and adds it to a specified
	 * UEFIFirmwareVolumeFileSystem.
	 *
	 * @param reader the specified BinaryReader
	 * @param fs     the specified UEFIFirmwareVolumeFileSystem
	 * @param parent the parent directory in the specified UEFIFirmwareVolumeFileSystem
	 */
	public FFSUISection(BinaryReader reader, UEFIFirmwareVolumeFileSystem fs,
			GFile parent) throws IOException {
		this(reader);

		// Add this section to the current FS.
		fs.addFile(parent, this, getName(), false);
	}

	/**
	 * Returns an InputStream for the contents of the current UI section.
	 *
	 * @return an InputStream for the contents of the current UI section
	 */
	public InputStream getData() {
		return new ByteArrayInputStream(uiText.getBytes());
	}

	/**
	 * Returns the text in the current UI section.
	 *
	 * @return the text in the current UI section
	 */
	public String getText() {
		return uiText;
	}

	/**
	 * Returns a string representation of the current UI section.
	 *
	 * @return a string representation of the current UI section
	 */
	@Override
	public String toString() {
		return super.toString() +
				"\nSection text: " + uiText;
	}
}
