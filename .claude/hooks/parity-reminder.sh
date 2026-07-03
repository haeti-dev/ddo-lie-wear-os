#!/bin/bash
# Remind about watchos parity when a parity-target wearos file is modified.
input=$(cat)
fp=$(printf '%s' "$input" | python3 -c "import sys,json;print(json.load(sys.stdin).get('tool_input',{}).get('file_path',''))" 2>/dev/null)

case "$fp" in
  */wearos/*Constants.kt|*/wearos/*Colors.kt|*/wearos/*Screen.kt|*/wearos/*NavHost.kt)
    echo "A parity-target wearos file was modified. Check whether watchos needs the same change (port-to-watchos / parity-check skills; rules: .claude/rules/parity.md)"
    ;;
esac
exit 0
