import React from 'react';
import { useTranslation } from 'react-i18next';
import {
    Form
} from 'react-bootstrap';

const FormikTextField = props =>  {
    const { t } = useTranslation();
    const { label, placeholder, name, error, touched, ...other } = props
    return (  
        <Form.Label>
            {t(`${label}`)}
            <Form.Control
                placeholder={ t(`${placeholder}`) } 
                name={ name }
                { ...other }
            />
            <p className="form-error">
            { error && touched && error && (
                t(`${error}`)
            )}
            </p>
        </Form.Label>
    );
}
 
export default FormikTextField;