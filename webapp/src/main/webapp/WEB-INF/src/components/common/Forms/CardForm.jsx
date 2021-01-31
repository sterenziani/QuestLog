import React from 'react';
import { useTranslation } from 'react-i18next';
import {
    Card,
    Form
} from 'react-bootstrap';

const CardForm = props => {
    const { titleKey, children, ...other } = props
    const { t } = useTranslation()
    return (
        <Card 
            style={{borderBottomLeftRadius: 30, borderBottomRightRadius: 30 }} 
            className="m-5 bg-light-grey right-wave left-wave" 
            bordered="true"
        >
            <Card.Header className="bg-very-dark text-white px-3 d-flex">
                <h2 className="share-tech-mono">
                    {t(`${titleKey}`)}
                </h2>
            </Card.Header>
            <Card.Body className="card-body d-flex flex-wrap padding-left-wave padding-right-wave">
                <Form className="w-100" {...other}>
                {
                    React.Children.map(children, child => child)
                }
                </Form>
            </Card.Body>
        </Card>
    );
}
 
export default CardForm;