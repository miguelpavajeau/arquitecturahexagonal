package com.pragma.powerup.infrastructure.configuration;

public enum Constants {
    ;
    public static final String NOMBRE = "El nombre no puede estar vacio";
    public static final String APELLIDO = "El apellido no puede estar vacio";
    public static final String DOCUMENTO_IDENTIDAD = "El documento de identidad no puede estar vacio";
    public static final String DOCUMENTO_IDENTIDAD_POSITIVO = "El documento de identidad debe ser un numero positivo";
    public static final String CELULAR = "El celular debe tener el formato +57 seguido de 10 digitos (ej: +573001234567)";
    public static final String EXPRESION_REGULAR_CELULAR = "^\\+57\\d{10}$";
    public static final String CORREO = "El correo debe ser un correo valido";
    public static final String CORREO_VACIO = "El correo no puede estar vacio";
    public static final String CLAVE = "La clave no puede estar vacia";
    public static final String EXPRESION_REGULAR_CORREO = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String RESTAURANTE_NOMBRE = "El nombre del restaurante no puede estar vacio";
    public static final String RESTAURANTE_NIT = "El NIT es obligatorio";
    public static final String RESTAURANTE_DIRECCION = "La direccion no puede estar vacia";
    public static final String RESTAURANTE_TELEFONO = "El telefono debe ser numerico, puede iniciar con + y tener maximo 13 caracteres";
    public static final String RESTAURANTE_URL_LOGO = "La url del logo no puede estar vacia";
    public static final String RESTAURANTE_ID_PROPIETARIO = "El id del propietario es obligatorio";
    public static final String EXPRESION_REGULAR_TELEFONO = "^\\+?[0-9]{1,13}$";

    public static final String PLATO_NOMBRE = "El nombre del plato no puede estar vacio";
    public static final String PLATO_PRECIO = "El precio debe ser un numero entero positivo mayor a 0";
    public static final String PLATO_DESCRIPCION = "La descripcion no puede estar vacia";
    public static final String PLATO_URL_IMAGEN = "La url de la imagen no puede estar vacia";
    public static final String PLATO_ID_CATEGORIA = "La categoria es obligatoria";
    public static final String PLATO_ID_RESTAURANTE = "El restaurante es obligatorio";
}
