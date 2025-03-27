#!/bin/bash
# godiddy-cli-package.sh
# Script to build a Debian package for godiddy-cli using native Debian packaging

# Exit on any error
set -e

# Parse command line arguments
VERSION="latest"  # Back to original setting
JAVA_VERSION="21"
STRIP_BINARIES=false

print_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo "Options:"
    echo "  -v, --version VERSION     Package version (default: latest)"
    echo "  -j, --java-version VER    Java version to depend on (default: 21)"
    echo "  -s, --strip               Strip debug symbols to reduce size"
    echo "  -h, --help                Show this help message"
}

while [[ $# -gt 0 ]]; do
    case $1 in
        -v|--version)
            VERSION="$2"
            shift 2
            ;;
        -j|--java-version)
            JAVA_VERSION="$2"
            shift 2
            ;;
        -s|--strip)
            STRIP_BINARIES=true
            shift
            ;;
        -h|--help)
            print_usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            print_usage
            exit 1
            ;;
    esac
done

# Set variables
BUILD_DIR="./build"
TEMP_DIR="${BUILD_DIR}/temp"
OUTPUT_DIR="${BUILD_DIR}/output"
PACKAGE_NAME="godiddy-cli"
JAR_FILE=$(find . -name "godiddy-cli-*-jar-with-dependencies.jar" | head -n 1)
FINAL_PACKAGE="${PACKAGE_NAME}_${VERSION}_amd64.deb"
MAINTAINER="DanubeTech <admin@danubetech.com>"

echo "Building package version: ${VERSION}"
echo "Java dependency version: ${JAVA_VERSION}"

# Create directory structure
echo "=== Creating build directory structure ==="
mkdir -p "${OUTPUT_DIR}"
mkdir -p "${TEMP_DIR}/${PACKAGE_NAME}/DEBIAN"
mkdir -p "${TEMP_DIR}/${PACKAGE_NAME}/usr/bin"
mkdir -p "${TEMP_DIR}/${PACKAGE_NAME}/usr/share/${PACKAGE_NAME}"
mkdir -p "${TEMP_DIR}/${PACKAGE_NAME}/usr/share/doc/${PACKAGE_NAME}"
mkdir -p "${TEMP_DIR}/${PACKAGE_NAME}/usr/lib/${PACKAGE_NAME}/lib"

# Bundle libjpeg-turbo8 library
echo "=== Bundling libjpeg-turbo8 library ==="
apt-get download libjpeg-turbo8
mkdir -p "${TEMP_DIR}/libjpeg"
dpkg-deb -x libjpeg-turbo8_*.deb "${TEMP_DIR}/libjpeg"

# Copy only the necessary library files
cp "${TEMP_DIR}"/libjpeg/usr/lib/x86_64-linux-gnu/libjpeg.so.8* "${TEMP_DIR}/${PACKAGE_NAME}/usr/lib/${PACKAGE_NAME}/lib/"

# Strip binaries if requested
if [ "$STRIP_BINARIES" = true ]; then
    echo "=== Stripping binaries to reduce size ==="
    find "${TEMP_DIR}/${PACKAGE_NAME}/usr/lib" -type f -name "*.so*" -exec strip --strip-unneeded {} \;
fi

rm -f libjpeg-turbo8_*.deb

# Create control file
echo "=== Creating package control file ==="
cat > "${TEMP_DIR}/${PACKAGE_NAME}/DEBIAN/control" << EOF
Package: ${PACKAGE_NAME}
Version: ${VERSION}
Section: utils
Priority: optional
Architecture: amd64
Depends: openjdk-${JAVA_VERSION}-jre | java${JAVA_VERSION}-runtime
Maintainer: ${MAINTAINER}
Description: Godiddy Command Line Interface
 A CLI tool for Decentralized Identifiers (DIDs) management.
 Provides utilities for creating, updating, and resolving DIDs.
EOF

# Create copyright file (minimal)
echo "=== Creating copyright file ==="
cat > "${TEMP_DIR}/${PACKAGE_NAME}/usr/share/doc/${PACKAGE_NAME}/copyright" << EOF
Upstream-Name: ${PACKAGE_NAME}
Source: https://github.com/danubetech/godiddy-cli
Copyright: $(date +%Y) DanubeTech
License: Apache-2.0
EOF

# Create compressed changelog to satisfy Debian requirements while minimizing size
echo "=== Creating minimal changelog file ==="
echo "${PACKAGE_NAME} (${VERSION}) stable; urgency=low

  * Initial release.

 -- ${MAINTAINER}  $(date -R)" | gzip -9 > "${TEMP_DIR}/${PACKAGE_NAME}/usr/share/doc/${PACKAGE_NAME}/changelog.gz"

# Copy JAR file without optimization
echo "=== Copying application files ==="
if [ -f "${JAR_FILE}" ]; then
    echo "=== Copying JAR file (no optimization) ==="
    cp "${JAR_FILE}" "${TEMP_DIR}/${PACKAGE_NAME}/usr/share/${PACKAGE_NAME}/"
else
    echo "Error: JAR file not found at ${JAR_FILE}"
    exit 1
fi

# Create launcher script
echo "=== Creating launcher script ==="
cat > "${TEMP_DIR}/${PACKAGE_NAME}/usr/bin/${PACKAGE_NAME}" << EOF
#!/bin/bash
# Set up library path to use bundled libraries
export LD_LIBRARY_PATH="/usr/lib/${PACKAGE_NAME}/lib:\$LD_LIBRARY_PATH"
# Execute the application with any arguments passed to this script
java --enable-preview -jar /usr/share/${PACKAGE_NAME}/${JAR_FILE} "\$@"
EOF
chmod +x "${TEMP_DIR}/${PACKAGE_NAME}/usr/bin/${PACKAGE_NAME}"

# Build the package with appropriate compression options
echo "=== Building Debian package ==="

# Use simple build command without compression options
echo "=== Building Debian package (standard compression) ==="
dpkg-deb --build "${TEMP_DIR}/${PACKAGE_NAME}" "${OUTPUT_DIR}/${FINAL_PACKAGE}"

# Clean up
echo "=== Cleaning up temporary files ==="
rm -rf "${TEMP_DIR}"

echo "=== Package successfully created ==="
echo "Final package: ${OUTPUT_DIR}/${FINAL_PACKAGE}"

# Show package details
echo "=== Package details ==="
dpkg -I "${OUTPUT_DIR}/${FINAL_PACKAGE}"
echo "=== Package size ==="
du -h "${OUTPUT_DIR}/${FINAL_PACKAGE}"
echo "=== Package contents ==="
dpkg -c "${OUTPUT_DIR}/${FINAL_PACKAGE}" | sort