import React, { Component } from 'react';
import AnyButton from "../AnyButton/AnyButton";
import { Row, Col } from 'react-bootstrap';
import GameService from "../../../services/api/gameService";

class Pagination extends Component {
    state = {
        url: this.props.url, // para saber donde me lleva cada boton
        currentPage : this.props.page, // para saber en cual estoy
        totalPages: this.props.totalPages, // para saber hasta que pagina se puede acceder
        queryParams: this.props.queryParams,
        pages: []
    }

    componentWillMount() {
        if (!this.state.totalPages || this.state.currentPage < 1) {
            return null;
        }
        let arr = [];
        for (let i = 1; i <= this.state.totalPages; i++) {
           arr.push(i);
        }
        this.setState({
            pages: [...arr]
        });
    }

    componentWillReceiveProps(newProps) {
        this.setState({
            url: newProps.url, // para saber donde me lleva cada boton
            currentPage : newProps.page, // para saber en cual estoy
            totalPages: newProps.totalPages, // para saber hasta que pagina se puede acceder
            queryParams: newProps.queryParams,
            pages: []
        })
    }

    render() {
        const prev = parseInt(this.state.currentPage) - 1;
        const next = parseInt(this.state.currentPage) + 1;
        let params = GameService.buildQueryParams(this.state.queryParams);
        return (
            <div className="col mb-5">
                 <div className="row text-center">
                     <Col>
                        <AnyButton className={prev <= 0? 'disabled' : ''} key={'prev'} href={`/` + this.state.url + `?page=` + prev +params} onClick={this.props.setPage} textKey="navigation.pagination.prev"/>
                     </Col>
                     <Col>
                         <Row>
                         {
                             this.state.pages.map(index => (
                                 <div className="col mx-auto">
                                    <AnyButton key={index}  className={this.isActive(index)} text={index} onClick={this.props.setPage} disabled={index === parseInt(this.state.currentPage)}
                                                    href={`/` + this.state.url + `?page=` + index +params} />
                                 </div>
                            ))
                        }
                        </Row>
                    </Col>
                    <Col>
                        <AnyButton className={next <= parseInt(this.state.totalPages)? '':'disabled'} key={'prev'}  href={`/` + this.state.url + `?page=` + next +params} onClick={this.props.setPage} textKey="navigation.pagination.next"/>
                    </Col>
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
