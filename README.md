# Peptide Calculator — Android

Aplicativo Android nativo em Kotlin + Jetpack Compose. Identidade visual clínica: branco, azul e cinza.

## Funcionalidades
- Identificação do produto.
- Entrada do conteúdo total do frasco (mg).
- Entrada do volume total de diluente (mL).
- Cálculo aritmético da concentração (mg/mL).
- Simulação do rendimento em porções teóricas com base em uma quantidade informada pelo usuário.
- Exibição do restante teórico.

## Limites do escopo
O aplicativo não recomenda nem valida doses, não calcula volume de aplicação ou unidades de seringa e não define frequência/esquema de administração. O rendimento é matemático e não considera perdas, volume residual, estabilidade, homogeneidade ou limitações do recipiente. Não utilizar como orientação para preparar ou administrar substâncias.

## Compilação local
Abra o projeto no Android Studio com JDK 17 e Android SDK 35. Para gerar o APK, execute **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

## GitHub Actions
Todo push na branch `main` ou execução manual em **Actions > Android APK > Run workflow** dispara `assembleDebug`. O arquivo `app-debug.apk` fica disponível por 14 dias no artefato `PeptideCalculator-debug-apk`.

Este workflow gera APK de depuração, não assinado para publicação na Play Store.
