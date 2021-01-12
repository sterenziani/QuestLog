import React, { Component } from 'react';
import {
    Card, Image,
} from 'react-bootstrap';
import ImageService from "../../../services/api/imageService";

class GenericListItem extends Component {
    state = {
        value: this.props.value,
        item: this.props.item,
        label: this.props.label,
        icon: null
    };

    componentWillMount() {
      ImageService.getImageLink(this.state.item.logo)
              .then((data) => {
                  this.setState({
                      icon: data
                  });
              }).then((data) =>  {});
    }

    render() {
        let cover = this.state.item.logo;
        if(this.state.label === "Publishers" || this.state.label === "Developers") {
            cover = false;
        }
        let lower = this.state.label.toLowerCase();
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '10rem'}}>
                <a className="d-flex flex-column flex-grow-1 text-white text-center align-center" href={`${process.env.PUBLIC_URL}/${lower}/` + this.state.value}>
                    { cover? [<div className="p-1" style={{height: '7rem'}}><Image className="list-card-icon" src={this.state.icon}  /></div>] : []}
                    <div className="card-body bg-primary flex-grow-1">
                        <h5 align={"center"}>{this.state.item.name}</h5>
                    </div>
                </a>
            </Card>
        )
    }
}

export default GenericListItem;
