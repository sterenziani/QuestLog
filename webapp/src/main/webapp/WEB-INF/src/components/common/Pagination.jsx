import React, { Component, Fragment } from 'react';

    class Pagination extends Component {
        state = {
        currentPage : this.props.page,
        total : this.props.total,
        pageLimit : 20,
        }

        render() {
        const pages = Math.ceil(this.state.total / this.state.pageLimit);

        if (!this.state.total || pages < 1) {
        return null;
        }

        const arr = Array.from(Array(pages).keys())


        return (
            <Fragment>
                <nav aria-label="Pagination">
                     <ul className="pagination">
                         { arr.map((index) => {
                            <li key={index+1} className={this.isActive(index+1)}>
                                {this.getIndex(index + 1)}
                            </li>

                        }) }
                    </ul>
                </nav>
            </Fragment>
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
                return "page-item active"
            }
            return "page-item"
        }
    }

    export default Pagination;