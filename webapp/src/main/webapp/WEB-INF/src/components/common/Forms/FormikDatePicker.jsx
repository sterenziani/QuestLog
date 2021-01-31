import DatePicker from "react-datepicker";
import moment from 'moment';
import "react-datepicker/dist/react-datepicker.css";
import {useState} from 'react';

const FormikDatePicker = ({ name, value, onChange, dateFormat, ...other }) => {
    const [startDate, setStartDate] = useState(null);
    return (
        <DatePicker
            className="form-control"
            placeholderText="YYYY-MM-DD"
            onChange={e => {
                onChange(name, moment(e).format("YYYY-MM-DD"));
                setStartDate(e);
            }}
            selected={startDate}
            dateFormat={dateFormat}
            {...other}
        />
    );
};

export default FormikDatePicker;