package org.buy.life.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    CREATE("CREATE", "{\"zh_cn\": \"已申请注册/未发送密码\",\"en\": \"Registered/Password not sent\",\"es\": \"Se ha solicitado el registro / no se ha enviado la contraseña\",\"fr\": \"Inscription demandée / mot de passe non envoyé\",\"de\": \"Registriert/Passwort nicht gesendet\"}"),

    COMPLETE("COMPLETE", "{\"zh_cn\": \"注册完成/已发送密码\",\"en\": \"Registration completed/password sent\",\"es\": \"Registro completado / contraseña enviada\",\"fr\": \"Inscription terminée / mot de passe envoyé\",\"de\": \"Registrierung abgeschlossen/Passwort gesendet\"}");
    /**
     * 后端code
     */
    private final String code;

    /**
     * desc 后端节点翻译
     */
    private final String desc;

    public static String getCodeByDesc(String desc) {
        for (UserStatusEnum skuStatusEnum : values()) {
            if (skuStatusEnum.desc.equals(desc)) {
                return skuStatusEnum.code;
            }
        }
        return null;
    }
}
