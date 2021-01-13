import React, { Component } from 'react';
import {Card, Row, Col, Container, Tabs, Tab, Button} from "react-bootstrap";
import {Translation} from "react-i18next";

class Pagination extends Component {
    state = {
        url: this.props.url, // para saber donde me lleva cada boton
        currentPage : this.props.page, // para saber en cual estoy
        totalPages: this.props.totalPages, // para saber hasta que pagina se puede acceder
        pages: []
    }

    componentWillMount() {
        console.log("En Pagination el url es: " +this.state.url);
        if (!this.state.totalPages || this.state.currentPage < 1) {
            return null;
        }
        let arr = [];
        for (var i = 1; i <= this.state.totalPages; i++) {
           arr.push(i);
        }
        this.setState({
            pages: [...arr]
        });
    }

    render() {
        const prev = this.state.currentPage - 1
        const next = this.state.currentPage + 1
        return (
            <div className="col mb-5">
                 <div className="row text-center">
                     {prev > 0 ? [<div className="col"> <Button key={'prev'} className="page-item">
                    {<Translation>
                    {
                        t => t("navigation.pagination.prev")
                    }
                        </Translation>}
                     </Button></div>] : null}
                         {
                             this.state.pages.map(index => (
                                 <div className="col mx-auto">
                                 <Button key={index} className={this.isActive(index)}
                                         disabled={index === this.state.currentPage}>
                                    {index}
                                 </Button>
                                 </div>
                            ))
                        }
                     {next < this.state.totalPages ? [<div className="col"><Button key={'next'} className="page-item">
                         {<Translation>
                             {
                                 t => t("navigation.pagination.next")
                             }
                         </Translation>}
                     </Button></div>] : null}
                </div>
            </div>
        );
    }


    isActive(index) {
        if(index === this.state.currentPage) {
            return "page-item active bg-primary"
        }
        return "page-item bg-primary"
    }

}

export default Pagination;
