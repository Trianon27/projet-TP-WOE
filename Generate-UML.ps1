# =====================================================================
# Generate-UML-Extended.ps1
# Génère un diagramme UML détaillé du projet WoE (branche haytam)
# Inclut attributs, méthodes, héritages et interfaces
# Produit les formats : PNG, SVG, PDF
# =====================================================================

$SrcDir = "src\main\java\org\centrale\objet\woe\projettp"
$OutDir = "uml"
$Puml   = Join-Path $OutDir "woe_classes.puml"
$Jar    = Join-Path $PSScriptRoot "plantuml.jar"

# -------------------- Vérifications --------------------
if (-not (Test-Path $SrcDir)) {
    Write-Error "❌ Dossier source introuvable : $SrcDir"
    exit
}
if (-not (Test-Path $Jar)) {
    Write-Error "❌ plantuml.jar introuvable à : $Jar"
    exit
}

# -------------------- Préparation --------------------
New-Item -ItemType Directory -Force -Path $OutDir | Out-Null
if (Test-Path $Puml) { Remove-Item $Puml -Force }

# -------------------- En-tête du diagramme --------------------
@"
@startuml
' --- Taille et qualité ---
scale max 4096 width
skinparam dpi 300

' --- Style général ---
skinparam backgroundColor #f8f8f8
skinparam classAttributeIconSize 0
skinparam classFontStyle bold
skinparam shadowing false
skinparam packageStyle rectangle
skinparam defaultFontName "Consolas"

title World of ECN - Diagramme de classes détaillé (branche haytam)
"@ | Out-File $Puml -Encoding UTF8

# -------------------- Extraction des classes --------------------
Get-ChildItem -Path $SrcDir -Recurse -Filter *.java | ForEach-Object {
    $content = Get-Content -Raw $_.FullName
    $content = [regex]::Replace($content, '/\*.*?\*/', '', 'Singleline') # Retirer commentaires /*
    $content = [regex]::Replace($content, '(?m)//.*$', '')              # Retirer commentaires //
    $content = [regex]::Replace($content, '\s+', ' ')                   # Normaliser espaces

    if ($content -match '\b(class|interface)\s+([A-Za-z_]\w*)') {
        $type = $matches[1]
        $name = $matches[2]

        # Héritage / implémentation
        $extends = if ($content -match 'extends\s+([A-Za-z_]\w*)') { $matches[1] } else { "" }
        $implements = if ($content -match 'implements\s+([A-Za-z_][\w\s,]*)') { $matches[1] } else { "" }

        # Déclaration de la classe / interface
        if ($type -eq "interface") {
            "interface $name {" | Out-File $Puml -Append -Encoding UTF8
        } elseif ($content -match '\babstract\b') {
            "abstract class $name {" | Out-File $Puml -Append -Encoding UTF8
        } else {
            "class $name {" | Out-File $Puml -Append -Encoding UTF8
        }

        # -------------------- Attributs --------------------
        foreach ($m in [regex]::Matches($content, '(public|protected|private)\s+([\w<>\[\]]+)\s+([A-Za-z_]\w*)\s*(=|;|\))')) {
            $vis = $m.Groups[1].Value
            $typeAttr = $m.Groups[2].Value
            $attr = $m.Groups[3].Value
            $symbol = switch ($vis) { 'public' {'+'} 'private' {'-'} 'protected' {'#'} default {''} }
            "    $symbol $attr : $typeAttr" | Out-File $Puml -Append -Encoding UTF8
        }

        # -------------------- Méthodes --------------------
        foreach ($m in [regex]::Matches($content, '(public|protected|private)\s+([\w<>\[\]]+)\s+([A-Za-z_]\w*)\s*\(([^)]*)\)\s*\{')) {
            $vis = $m.Groups[1].Value
            $ret = $m.Groups[2].Value
            $meth = $m.Groups[3].Value
            $args = $m.Groups[4].Value.Trim()
            $symbol = switch ($vis) { 'public' {'+'} 'private' {'-'} 'protected' {'#'} default {''} }
            if ($args -eq "") { $args = " " }
            "    $symbol $meth($args) : $ret" | Out-File $Puml -Append -Encoding UTF8
        }

        # Fin de la classe
        "}" | Out-File $Puml -Append -Encoding UTF8
        ""  | Out-File $Puml -Append -Encoding UTF8

        # -------------------- Relations --------------------
        if ($extends) { "$name --|> $extends" | Out-File $Puml -Append -Encoding UTF8 }
        if ($implements) {
            $implements -split "," | ForEach-Object {
                $i = $_.Trim()
                if ($i) { "$name ..|> $i" | Out-File $Puml -Append -Encoding UTF8 }
            }
        }
    }
}

# -------------------- Pied du fichier --------------------
"@enduml" | Out-File $Puml -Append -Encoding UTF8

# -------------------- Génération des fichiers --------------------
Write-Host "✅ Fichier PUML généré : $Puml"

Write-Host "🖼️  Génération du diagramme SVG..."
& java -jar $Jar -tsvg $Puml | Out-Null

Write-Host "🖼️  Génération du diagramme PNG..."
& java -jar $Jar -tpng $Puml | Out-Null

$png = Join-Path $OutDir "woe_classes.png"
if (Test-Path $png) {
    Write-Host "🎉 Diagrammes générés dans : $OutDir"
    Write-Host "   ├── woe_classes.png"
    Write-Host "   ├── woe_classes.svg"
    Write-Host "   └── woe_classes.pdf"
} else {
    Write-Warning "⚠️  PlantUML n’a pas pu produire les images. Vérifie Graphviz."
}
