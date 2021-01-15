import React, { Component } from 'react';
import AnyButton from "../AnyButton/AnyButton";

class Pagination extends Component {
    state = {
        url: this.props.url, // para saber donde me lleva cada boton
        currentPage : this.props.page, // para saber en cual estoy
        totalPages: this.props.totalPages, // para saber hasta que pagina se puede acceder
        pages: []
    }

    componentWillMount() {
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
        const prev = parseInt(this.state.currentPage) - 1;
        const next = parseInt(this.state.currentPage) + 1;
        return (
            <div className="col mb-5">
                 <div className="row text-center">
                     {prev > 0 ? [<div className="col">
                         <AnyButton key={'prev'} href={`/` + this.state.url + `?page=` + prev} onClick={this.props.setPage} textKey="navigation.pagination.prev"/>
                         </div>] : null}
                         {
                             this.state.pages.map(index => (
                                 <div className="col mx-auto">
                                         <AnyButton key={index}  className={this.isActive(index)} text={index} onClick={this.props.setPage} disabled={index === parseInt(this.state.currentPage)}
                                                    href={`/` + this.state.url + `?page=` + index} />
                                 </div>
                            ))
                        }
                     {next <= parseInt(this.state.totalPages) ? [<div className="col">
                         <AnyButton key={'prev'}  href={`/` + this.state.url + `?page=` + next} onClick={this.props.setPage} textKey="navigation.pagination.next"/>
                     </div>] : null}
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
