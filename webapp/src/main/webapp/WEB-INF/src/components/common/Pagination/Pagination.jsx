import React, { Component } from 'react';
import {Card, Row, Col, Container, Tabs, Tab, Button} from "react-bootstrap";

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
        return (
            <div>
            <nav aria-label="Pagination">
                 <Row className="pagination">
                 <Button key={'prev'} className="page-item">
                    <p>{'< TRADUCIME'}</p>
                 </Button>
                 {
                     this.state.pages.map(index => (
                         <Button key={index} className={this.isActive(index)}>
                            {index}
                         </Button>
                    ))
                }
                <Button key={'next'} className="page-item">
                   <p>{'TRADUCIME >'}</p>
                </Button>
                </Row>
            </nav>
            </div>
        );
    }

    getIndex(index) {
        const prev = this.state.currentPage - 1
        const next = this.state.currentPage + 1

        if(index === prev) {
            return (<a className="page-link" href="#">
                    <span className="sr-only">Previous</span>
                </a>);
        }
        else if(index === next) {
            return (<a className="page-link" href="#">
                <span className="sr-only">Next</span>
            </a>);
        }

        return (<a className="page-link" href="#">{ index }</a>);
    }

    isActive(index) {
        if(index === this.state.currentPage) {
            return "page-item active bg-primary"
        }
        return "page-item bg-light"
    }
}

export default Pagination;
