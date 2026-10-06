# Islamic Digital Clock
**Made by Mufti Hafiz Muhammad Shoaib Khan Alai**

## خصوصیات
- ڈیجیٹل گھڑی اور اگلی نماز کا کاؤنٹ ڈاؤن
- ہجری (اُمّ القریٰ) اور عیسوی تاریخیں، رویت کے مطابق ±2 دن ایڈجسٹمنٹ
- نماز کے اوقات (Adhan لائبریری، کراچی میتھڈ، حنفی عصر) — لوکیشن نہ ملے تو لاہور
- اذان نوٹیفکیشن (پس منظر میں بھی، ریبوٹ کے بعد بھی) اور آن/آف سوئچ
- اسلامی ایام کی ہائی لائٹنگ (رمضان، عیدین، عاشورہ وغیرہ)

## اذان کی آواز
`app/src/main/res/raw/adhan.mp3` فی الحال خالی فائل ہے۔ اپنی پسند کی اذان کی mp3 اسی نام سے رکھ دیں۔
جب تک آواز نہ رکھی جائے، فون کی ڈیفالٹ نوٹیفکیشن آواز بجے گی۔

## Build
```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```
یا GitHub پر push کریں؛ Actions خود APK بنا دے گا۔

## GitHub پر اپلوڈ کرنے کا طریقہ
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/yourusername/IslamicDigitalClock.git
git push -u origin main
```
