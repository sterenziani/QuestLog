import React, { Component } from 'react';
import { 
    withTranslation 
} from 'react-i18next';
import { 
    Formik 
} from 'formik';
import * as Yup from 'yup';
import {
    Form,
    Spinner
} from 'react-bootstrap';
import { 
    Helmet, 
    HelmetProvider 
} from "react-helmet-async";

import AnyButton            from '../../common/AnyButton/AnyButton';

import CardForm             from '../../common/Forms/CardForm';
import FormikDatePicker     from '../../common/Forms/FormikDatePicker';
import FormikMultiSelect    from '../../common/Forms/FormikMultiSelect';

import withUser             from '../../hoc/withUser';

import GameService          from '../../../services/api/gameService';
import RegionService        from '../../../services/api/regionService';
import PlatformService      from '../../../services/api/platformService';
import DeveloperService     from '../../../services/api/devService';
import PublisherService     from '../../../services/api/publisherService';
import GenreService         from '../../../services/api/genreService';

class NewGamePage extends Component {
    state = {
        loading_releases    : true,
        loading_platforms   : true,
        loading_developers  : true,
        loading_publishers  : true,
        loading_genres      : true,
        releases   : [],
        platforms  : [],
        developers : [],
        publishers : [],
        genres     : []
    }

    componentDidMount = () => {
        this.fetchFromAPI();
    }

    fetchFromAPI    = async () => {
        await this.loadRegions()
        await this.loadPlatforms()
        await this.loadDevelopers()
        await this.loadPublishers()
        await this.loadGenres()
    }

    loadRegions     = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await RegionService.getEveryRegion()
            if(response.status){
                gotResponse = false;
            }
            await new Promise(r => setTimeout(r, 5000));
        } while (!gotResponse);

        const regions = response.map(r => ({ 'id' : r.id, 'region' : r.shortName }))
        this.setState({
            releases : regions,
            loading_releases : false
        })
    } 

    loadPlatforms   = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await PlatformService.getEveryPlatform()
            if(response.status){
                gotResponse = false;
            }
            await new Promise(r => setTimeout(r, 5000));
        } while (!gotResponse);

        const platforms = response.map(p => ({ 'label' : p.name, 'value' : p.id }))
        this.setState({
            platforms : platforms,
            loading_platforms : false
        })
    }

    loadDevelopers  = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await DeveloperService.getEveryDeveloper()
            if(response.status){
                gotResponse = false;
            }
            await new Promise(r => setTimeout(r, 5000));
        } while (!gotResponse);

        const developers = response.map(d => ({ 'label' : d.name, 'value' : d.id }))
        this.setState({
            developers : developers,
            loading_developers : false
        })
    }

    loadPublishers  = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await PublisherService.getEveryPublisher()
            if(response.status){
                gotResponse = false;
            }
            await new Promise(r => setTimeout(r, 5000));
        } while (!gotResponse);

        const publishers = response.map(p => ({ 'label' : p.name, 'value' : p.id }))
        this.setState({
            publishers : publishers,
            loading_publishers : false
        })
    }

    loadGenres      = async () => {
        let gotResponse = true;
        let response;
        do {
            response = await GenreService.getEveryGenre()
            if(response.status){
                gotResponse = false;
            }
            await new Promise(r => setTimeout(r, 5000));
        } while (!gotResponse);

        const genres = response.map(g => ({ 'label' : g.name, 'value' : g.id }))
        this.setState({
            genres : genres,
            loading_genres : false
        })
    }

    impactAPI = async (values) => {
        let releases = [];
        this.state.releases.forEach(r => {
            const releaseDate = values.region[r.id]
            releases.push({'locale': r.id, 'date': releaseDate})
        });

        const toBase64 = file => new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result);
            reader.onerror = error => reject(error);
        });

        const cover         = values.cover.file ? await toBase64(values.cover.file) : null;
        const platforms     = values.platforms ? values.platforms.map(p => p.value) : [];
        const developers    = values.developers ? values.developers.map(d => d.value) : [];
        const publishers    = values.publishers ? values.publishers.map(p => p.value) : [];
        const genres        = values.genres ? values.genres.map(g => g.value) : [];

        await GameService.register(values.title, values.description, cover, values.trailer, platforms, developers, publishers, genres, releases);
    }

    onSubmit = (values, { setSubmitting }) => {
        setSubmitting(false);
        this.impactAPI(values);
    }

    onFileChanged = (event, setFieldValue) => {
        if(event.target.file && event.target.file[0]){
            if(event.target.files[0].size > 4000000){
                //TODO: Show error message
                console.log('File too big')
            } else {
                setFieldValue('cover.file', event.target.files[0])
                setFieldValue('cover.fileName', event.target.files[0].name)
            }
        }
    }

    render() {
        const { t } = this.props
        return (
            <React.Fragment>
                <HelmetProvider>
                    <Helmet>
                        <title>{t('createGame.title')} - QuestLog</title>
                    </Helmet>
                </HelmetProvider>
                <Formik
                    initialValues = {{
                            title       : '',
                            description : '',
                            cover       : {
                                file     : '',
                                fileName : ''
                            },
                            trailer     : '',
                            region      : {
                                1 : ''
                            },
                            platforms   : [],
                            developers  : [],
                            publishers  : [],
                            genres      : []
                        }
                    }
                    onSubmit = { this.onSubmit }
                >
                {({
                    values,
                    errors,
                    touched,
                    handleChange,
                    handleBlur,
                    handleSubmit,
                    isSubmitting,
                    setFieldValue,
                    setFieldTouched
                }) => (
                    <CardForm
                        titleKey="createGame.title"
                        onSubmit={ handleSubmit }
                    >
                        <Form.Group controlId="formGameTitle">
                            <Form.Label><strong>Title</strong></Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder="Title" 
                                name="title"
                                value={ values.title }
                                error={ errors.title }
                                touched={ touched.title }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameDescription">
                            <Form.Label><strong>Description</strong></Form.Label>
                            <Form.Control 
                                as="textarea" 
                                rows={8} 
                                placeholder="Description"
                                name="description"
                                value={ values.description }
                                error={ errors.description }
                                touched={ touched.description }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label><strong>Cover</strong></Form.Label>
                            <Form.File 
                                id="cover"
                                name="cover"
                                label={ values.cover.fileName || "Choose a file..." }
                                data-browse="Browse"
                                accept="image/*"
                                onChange={ e => this.onFileChanged(e, setFieldValue) }
                                onBlur={ handleBlur } 
                                custom
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameTrailer">
                            <Form.Label><strong>Trailer</strong></Form.Label>
                            <Form.Control 
                                type="text" 
                                placeholder="YouTube video ID" 
                                name="trailer"
                                value={ values.trailer }
                                error={ errors.trailer }
                                touched={ touched.trailer }
                                onChange={ handleChange }
                                onBlur={ handleBlur } 
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label><strong>Preview</strong></Form.Label>
                            <div>
                                <iframe 
                                    id="preview" 
                                    width="286" 
                                    height="161" 
                                    src={`https://www.youtube.com/embed/${values.trailer}`} 
                                    frameBorder="0" 
                                    allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" 
                                    allowFullScreen="">    
                                </iframe>
                            </div>
                        </Form.Group>
                        <Form.Group controlId="formGamePlatforms">
                            <Form.Label><strong>Platforms</strong></Form.Label>
                            <FormikMultiSelect
                                id="platforms"
                                name="platforms"
                                placeholder="Select platforms"
                                options={this.state.platforms}
                                value={values.platforms}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.platforms}
                                error={errors.platforms}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_platforms}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameDevelopers">
                            <Form.Label><strong>Developers</strong></Form.Label>
                            <FormikMultiSelect
                                id="developers"
                                name="developers"
                                placeholder="Select developers"
                                options={this.state.developers}
                                value={values.developers}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.developers}
                                error={errors.developers}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_developers}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGamePublishers">
                            <Form.Label><strong>Publishers</strong></Form.Label>
                            <FormikMultiSelect
                                id="publishers"
                                name="publishers"
                                placeholder="Select publishers"
                                options={this.state.publishers}
                                value={values.publishers}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.publishers}
                                error={errors.publishers}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_publishers}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameGenres">
                            <Form.Label><strong>Genres</strong></Form.Label>
                            <FormikMultiSelect
                                id="genres"
                                name="genres"
                                placeholder="Select genres"
                                options={this.state.genres}
                                value={values.genres}
                                isMulti={true}
                                onChange={setFieldValue}
                                onBlur={setFieldTouched}
                                touched={touched.genres}
                                error={errors.genres}
                                isClearable={true}
                                backspaceRemovesValue={true}
                                isDisabled={this.state.loading_genres}
                            />
                        </Form.Group>
                        <Form.Group controlId="formGameReleaseDate">
                            <Form.Label><strong>Release Date</strong></Form.Label>
                            <div>
                            {
                                this.state.releases.map(release => (
                                    <Form.Group key={release.id} controlId={`formGameReleaseDate${release.id}`} className="d-flex align-items-stretch flex-column">
                                        <Form.Label>{release.region}</Form.Label>
                                        <div className="d-flex flex-column align-items-stretch">
                                            <FormikDatePicker
                                                dateFormat="yyyy-MM-dd"
                                                id={`region.${release.id}`}
                                                name={`region.${release.id}`}
                                                value={values.region[release.id]}
                                                onChange={setFieldValue}
                                                isDisabled={this.state.loading_releases}
                                            />
                                        </div>
                                        
                                    </Form.Group>
                                ))
                            }
                            </div>
                        </Form.Group>
                        <Form.Group>
                            <AnyButton 
                                variant="primary"
                                type="submit"
                                textKey="createGame.add"
                                disabled={ isSubmitting || this.state.loading_releases 
                                    || this.state.loading_platforms || this.state.loading_developers
                                    || this.state.loading_publishers || this.state.loading_genres }
                                block={true}
                            />
                        </Form.Group>
                    </CardForm>
                )} 
                </Formik>
            </React.Fragment>
        );
    }
}
 
export default withTranslation() (withUser(NewGamePage, { visibility: "adminOnly" }));