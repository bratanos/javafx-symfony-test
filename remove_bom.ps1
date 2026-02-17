# Remove BOM from all Java files
$files = Get-ChildItem -Path . -Filter *.java -Recurse

Write-Host "Found $($files.Count) Java files"

foreach ($file in $files) {
    try {
        # Read file as bytes
        $bytes = [System.IO.File]::ReadAllBytes($file.FullName)

        # Check if file starts with UTF-8 BOM (EF BB BF)
        if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
            Write-Host "Removing BOM from: $($file.FullName)"

            # Remove first 3 bytes (BOM)
            $newBytes = $bytes[3..($bytes.Length - 1)]

            # Write back without BOM
            [System.IO.File]::WriteAllBytes($file.FullName, $newBytes)
            Write-Host "  ✓ Fixed" -ForegroundColor Green
        }
    }
    catch {
        Write-Host "  ✗ Error processing $($file.FullName): $_" -ForegroundColor Red
    }
}

Write-Host "`nDone! All Java files processed." -ForegroundColor Cyan
