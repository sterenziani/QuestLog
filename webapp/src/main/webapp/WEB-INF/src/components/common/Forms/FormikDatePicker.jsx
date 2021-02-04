import DatePicker from "react-datepicker";
import moment from 'moment';
import "react-datepicker/dist/react-datepicker.css";
import {useState} from 'react';

const FormikDatePicker = ({ name, value, onChange, dateFormat, ...other }) => {
    const date = value !== undefined ? new Date(moment(value).format("YYYY/MM/DD").toString()) : null;
    const [startDate, setStartDate] = useState(date);
    return (
        <DatePicker
            className="form-control"
            placeholderText="YYYY-MM-DD"
            onChange={e => {
                if(e === null){
                    onChange(name, null);
                    setStartDate(null);
                } else {
                    onChange(name, moment(e).format("YYYY-MM-DD"));
                    setStartDate(e);
                }
            }}
            selected={startDate}
            startDate={startDate}
            dateFormat={dateFormat}
            {...other}
        />
    );
};

export default FormikDatePicker;