# Code Formatting Guide

This project uses [Spotless](https://github.com/diffplug/spotless) with Google Java Format to automatically format code, similar to how Prettier works for JavaScript projects.

## Automatic Formatting

The project is configured to use Google's Java formatter to ensure consistent code style across all Java files.

### Key Features

- **Consistent Formatting**: All Java code is formatted according to Google Java Format standards
- **Import Organization**: Imports are automatically organized and unused imports are removed
- **Automatic Enforcement**: The build will fail if code doesn't meet the formatting standards

## How to Use

### Check Formatting

To check if your code meets the formatting standards without making changes:

```bash
./gradlew spotlessCheck
```

This will report any formatting violations but won't modify your files.

### Apply Formatting

To automatically format all Java files according to the standards:

```bash
./gradlew spotlessApply
```

This will reformat all Java files to meet the configured standards.

### Pre-commit Hook (Recommended)

It's recommended to set up a pre-commit hook to automatically format code before committing.

#### Option 1: Use the Setup Script

Run the provided setup script:

```bash
# Make the script executable first
chmod +x setup-formatting-hook.sh
# Run the script
./setup-formatting-hook.sh
```

This script will automatically create and configure the pre-commit hook for you.

#### Option 2: Manual Setup

1. Create a file named `.git/hooks/pre-commit` with the following content:

```bash
#!/bin/sh
./gradlew spotlessApply
git add -u
```

2. Make it executable:

```bash
chmod +x .git/hooks/pre-commit
```

## IDE Integration

### IntelliJ IDEA

1. Install the "google-java-format" plugin
2. Go to Settings → Editor → Code Style → Java
3. Click on "Import Scheme" → "Google Style"

The project is already configured to automatically format code on save in IntelliJ IDEA. This is done through the following configuration files:
- `.idea/codeStyles/Project.xml` - Defines the code style based on Google Java Format
- `.idea/codeStyles/codeStyleConfig.xml` - Enables the project code style
- `.idea/workspace.xml` - Enables the "Reformat code on save" option

### VS Code

1. Install the "Java Extension Pack"
2. Install the "Google Java Format" extension

The project is already configured to automatically format code on save in VS Code through the `.vscode/settings.json` file, which includes the following settings:
- `"editor.formatOnSave": true` - Enables code formatting on save
- `"java.format.enabled": true` - Enables Java formatting
- `"java.format.settings.profile": "GoogleStyle"` - Uses Google's Java style guide

## Customizing the Format

The formatting rules are configured in the `build.gradle` file. If you need to customize the formatting:

1. Open `build.gradle`
2. Modify the `spotless` configuration block

## Troubleshooting

If you encounter issues with formatting:

1. Make sure you're using the correct Java version (Java 11 or higher)
2. Run `./gradlew --refresh-dependencies` to refresh dependencies
3. Check the Spotless documentation for specific issues
