# Catatan Port ke 1.21.1 & Diagnosa Log

## 1. Diagnosa `latest_game.log`

- Instance yang crash sebenarnya berjalan di **Minecraft 1.20.1 + Fabric Loader 0.19.5**, lewat
  Zalith Launcher di Android (arm64, MobileGL/Zink) — bukan 1.16.5 seperti yang tertulis di
  `gradle.properties` project ini.
- Tidak ada satupun Java `Exception`/stack trace di log. Baris terakhir adalah:
  ```
  Remapping optifine from official to intermediary
  Failed to preload me.modmuss50.optifabric.shadow.tinyremapper.Propagator
  ```
  lalu log berhenti total. Ini pola khas **proses game mati paksa (OOM-kill oleh Android / crash
  native driver)** di tengah langkah OptiFabric me-remap ulang class OptiFine — bukan sebuah
  Java error yang bisa "di-fix" dengan patch kode. Ini dikenal terjadi kalau:
  - RAM yang dialokasikan ke instance kurang (proses tiny-remapper + OptiFine + Fabric API
    sekaligus di memori device mobile cukup berat).
  - Driver render translasi (Zink/MobileGL) kehabisan memori GPU/host saat proses berlangsung.
- Langkah yang realistis untuk mengatasi ini **di sisi game**, bukan di source mod:
  1. Naikkan alokasi RAM instance di Zalith Launcher (coba 3–4 GB kalau device memungkinkan).
  2. Hapus folder `.optifine` di `.minecraft` supaya cache remap lama tidak korup, lalu coba lagi.
  3. Pastikan versi OptiFine (`OptiFine_1.20.1_HD_U_I6`), Fabric Loader, Fabric API, dan
     OptiFabric benar-benar versi yang saling cocok (versi campuran adalah penyebab #1 crash
     OptiFabric secara umum).
  4. Kalau tetap crash, coba tanpa OptiFine dulu (Sodium + Iris) untuk memastikan itu memang
     penyebabnya, bukan mod Fabric lain.

## 2. Status port ke 1.21.1 — perlu jujur soal batasannya

Environment kerja saya di sini **tidak punya akses internet/Gradle** (tidak bisa `./gradlew
build`, tidak bisa unduh Minecraft/Yarn/Fabric API/OptiFine), jadi saya tidak bisa mengkompilasi
atau menguji hasil port ini. Yang sudah saya ubah sebagai titik awal:

- `gradle.properties`: `minecraft_version=1.21.1`, `yarn_mappings=1.21.1+build.3`,
  `fabric_version=0.116.10+1.21.1`, `loader_version=0.16.9` (cek versi loader terbaru di
  fabricmc.net/use sebelum build, ini bisa saja sudah naik lagi).
- `build.gradle`: `sourceCompatibility`/`targetCompatibility` dinaikkan ke Java 21 (wajib untuk
  1.20.5+).

Yang **belum** dan realistanya tidak bisa saya selesaikan tanpa kompilasi nyata:

- Blok `compileJava { mappings { ... } }` di `build.gradle` berisi puluhan nama kelas/metode
  intermediary (`class_5636`, `class_7775`, dst.) yang di-hardcode khusus untuk mapping 1.16.5.
  Nomor intermediary **tidak stabil antar versi** — semua ini harus digenerate ulang dengan
  mendekompilasi Minecraft 1.21.1 asli, satu per satu, dan dicocokkan lagi dengan struktur kelas
  OptiFine 1.21.1.
- Puluhan file `src/main/resources/optifabric.*.mixins.json` dan kelas compat di
  `src/main/java/.../compat/` menyasar signature method OptiFine versi lama (HD_U_I6, dst).
  OptiFine 1.21.1 punya struktur internal yang berbeda (rendering pipeline Minecraft berubah
  besar sejak 1.17–1.21), jadi mixin-mixin ini nyaris pasti perlu ditulis ulang satu per satu
  sambil membandingkan dengan OptiFine 1.21.1 asli.
- Proyek resminya (`Chocohead/OptiFabric`, branch `llama` — persis file yang kamu upload) sudah
  jarang di-update (PR terakhir 2023), dan OptiFine sendiri makin sulit dipadukan dengan Fabric
  di versi-versi baru karena arsitektur rendering Fabric API terus berubah.

**Rekomendasi jujur:** port penuh & teruji butuh siklus compile → jalankan → lihat error →
perbaiki mixin satu-satu di mesin lokal kamu (dengan koneksi internet buat Gradle). Saya sudah
menyiapkan kerangka gradle + workflow CI di bawah supaya siklus itu bisa langsung kamu mulai,
tapi jangan berharap file ini langsung jalan tanpa iterasi lebih lanjut. Alternatif yang jauh
lebih stabil di 1.21.1: **Sodium + Iris + shader OptiFine-compatible**, karena itulah yang
sebagian besar komunitas modding Fabric pakai sekarang menggantikan OptiFine.

## 3. `build.yml` untuk GitHub Actions

Sudah dibuat di `.github/workflows/build.yml`: pakai JDK 21, `actions/checkout@v4`,
`actions/setup-java@v4`, `gradle/actions/setup-gradle@v4` (dengan cache), lalu upload jar hasil
build sebagai artifact, plus upload log kalau build gagal supaya gampang didiagnosis.
