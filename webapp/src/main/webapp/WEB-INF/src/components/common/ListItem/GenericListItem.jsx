import React, { Component } from 'react';
import {
    Card, Image,
} from 'react-bootstrap';

class GenericListItem extends Component {
    state = {
        value: this.props.value,
        item: this.props.item,
        label: this.props.label,
    };

    render() {
        let cover = this.state.item.logo;
        if(this.state.label === "Publishers" || this.state.label === "Developers") {
            cover = null;
        }
        let lower = this.state.label.toLowerCase();
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '10rem'}}>
                <a className="d-flex flex-column flex-grow-1 text-white align-content-center" href={`${process.env.PUBLIC_URL}/${lower}/` + this.state.value}>
                    <Image src={cover} style={{height: '8rem'}} fluid />
                    <div className="card-body bg-primary flex-grow-1">
                        <h5 align={"center"}>{this.state.item.name}</h5>
                    </div>
                </a>
            </Card>
        )
    }
}

export default GenericListItem;