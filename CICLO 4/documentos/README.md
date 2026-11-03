# SlotMachine – Proyecto inicial DOPO 2026-2 (Ciclo 5)
Autores: Samuel Mena Serrato, Wilson Morales Sánchez

## Abrir en VS Code
1. Instalar JDK 17 y la extensión "Extension Pack for Java".
2. File > Open Folder > esta carpeta (la que tiene pom.xml). VS Code lo reconoce como proyecto Maven.
3. Pestaña Testing (matraz) > Run All Tests.

## Pruebas
- SlotMachineC1Test, SlotMachineC2Test, SlotMachineCC2Test, SlotMachineC3Test,
  SlotMachineC4Test, SlotMachineCC4Test, SlotMachineContestTest, SlotMachineContestCTest
- Aceptación: SlotMachineAcceptanceTest (abre la ventana y recorre los escenarios).

## Informes (correr en la terminal de VS Code)
- Cubrimiento JaCoCo:  mvn clean test
  -> abrir target/site/jacoco/index.html
- Reglas PMD:          mvn pmd:pmd     -> target/site/pmd.html
  Verificar prioridad alta (rojo): mvn pmd:check  (falla si queda alguna)

Los informes de entrega están en informes/: informe-analisis-dinamico.pdf, informe-analisis-estatico.pdf, y los reportes HTML en informes/inicial y informes/final.

## Entrega (Ciclo 5)
- Diseño: slotMachine.asta
- Informes: informes/informe-analisis-dinamico.pdf e informes/informe-analisis-estatico.pdf (HTML en informes/inicial y informes/final)
- Retrospectiva: retrospectiva.pdf
