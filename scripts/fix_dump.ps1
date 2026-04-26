param(
    [string]$InputFile = "..\db\AcerosSQL.sql",
    [string]$OutputFile = "..\db\AcerosSQL.fixed.sql",
    [string]$OldCollation = "utf8mb4_0900_ai_ci",
    [string]$NewCollation = "utf8mb4_unicode_ci"
)

# Run from project root: powershell -ExecutionPolicy Bypass -File .\scripts\fix_dump.ps1

if (-not (Test-Path $InputFile)) {
    Write-Error "Input file not found: $InputFile"
    exit 1
}

try {
    Write-Host "Reading $InputFile ..."
    $text = Get-Content -Path $InputFile -Raw -Encoding UTF8
    Write-Host "Replacing '$OldCollation' with '$NewCollation' ..."
    $fixed = $text -replace [regex]::Escape($OldCollation), $NewCollation
    Write-Host "Writing $OutputFile ..."
    $fixed | Set-Content -Path $OutputFile -Encoding UTF8
    Write-Host "Done: $OutputFile"
} catch {
    Write-Error $_.Exception.Message
    exit 1
}