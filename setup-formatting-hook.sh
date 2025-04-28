#!/bin/sh
# Script to set up the pre-commit hook for automatic code formatting

# Create hooks directory if it doesn't exist
mkdir -p .git/hooks

# Create the pre-commit hook
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/sh
# Pre-commit hook to automatically format code using Spotless

echo "Running Spotless to format code..."
./gradlew spotlessApply

# Add all formatted files back to the staging area
git add -u

echo "Code formatting complete."
EOF

# Make the pre-commit hook executable
chmod +x .git/hooks/pre-commit

echo "Pre-commit hook for automatic code formatting has been set up successfully."
echo "The hook will run automatically before each commit to ensure code is properly formatted."