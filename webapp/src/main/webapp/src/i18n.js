import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from 'i18next-browser-languagedetector';

const resources = {
    en: {
        translation: {
            "welcome": "Welcome!"
        }
    },
    es: {
        translation: {
            "welcome": "¡Bienvenido!"
        }
    }
}

i18n
    .use(initReactI18next)
    .use(LanguageDetector)
    .init({
        resources,
        supportedLngs            : ["en", "es"],
        nonExplicitSupportedLngs : true, 
        fallbackLng              : "en",
        keySeparator             : false,
        interpolation            : {
            escapeValue: false
        }
    });

export default i18n;