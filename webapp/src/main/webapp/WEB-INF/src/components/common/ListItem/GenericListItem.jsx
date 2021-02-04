import React, { Component } from 'react';
import { Card, Image } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import ImageService from "../../../services/api/imageService";
import {Translation} from "react-i18next";
import Spinner from "react-bootstrap/Spinner";

class GenericListItem extends Component {
    state = {
        value: this.props.value,
        item: this.props.item,
        category: this.props.category,
        icon: null,
        loading: true,
    };

    componentWillMount() {
      ImageService.getImageLink(this.state.item.logo)
              .then((data) => {
                  this.setState({
                      icon: data,
                      loading: false,
                  });
              }).then((data) =>  {});
    }

    render() {
        if (this.state.loading === true) {
            return <div style={{
                position: 'absolute', left: '50%', top: '50%',
                transform: 'translate(-50%, -50%)'}}>
                <Spinner animation="border" variant="primary" />
            </div>
        }
        let cover = this.state.item.logo;
        let label = this.state.item.name;
        if(this.state.category === "Publishers" || this.state.category === "Developers") {
            cover = false;
        }
        if(this.state.category === "Genres") {
            label = <Translation>{t => t("genres." +this.state.item.name)}</Translation>;
        }
        let lower = this.state.category.toLowerCase();
        return (
            <Card className="m-3 d-flex bg-transparent" style={{width: '10rem'}}>
                <LinkContainer to={`/${lower}/` + this.state.value}>
                    <a href={() => false} className="d-flex flex-column flex-grow-1 text-white text-center align-center">
                        { cover? [<div className="p-1" style={{height: '7rem'}}><Image className="list-card-icon" src={this.state.icon}  /></div>] : []}
                        <div className="card-body bg-primary flex-grow-1">
                            <h5 align={"center"}> {label} </h5>
                        </div>
                    </a>
                </LinkContainer>
            </Card>
        )
    }
}

export default GenericListItem;
