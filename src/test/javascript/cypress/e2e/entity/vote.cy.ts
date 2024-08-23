import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Vote e2e test', () => {
  const votePageUrl = '/vote';
  const votePageUrlPattern = new RegExp('/vote(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const voteSample = { voteType: 'really aside indeed' };

  let vote;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/votes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/votes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/votes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/votes/${vote.id}`,
      }).then(() => {
        vote = undefined;
      });
    }
  });

  it('Votes menu should load Votes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vote');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Vote').should('exist');
    cy.url().should('match', votePageUrlPattern);
  });

  describe('Vote page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(votePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Vote page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vote/new$'));
        cy.getEntityCreateUpdateHeading('Vote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', votePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/votes',
          body: voteSample,
        }).then(({ body }) => {
          vote = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/votes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [vote],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(votePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Vote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vote');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', votePageUrlPattern);
      });

      it('edit button click should load edit Vote page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', votePageUrlPattern);
      });

      it('edit button click should load edit Vote page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vote');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', votePageUrlPattern);
      });

      it('last delete button click should delete instance of Vote', () => {
        cy.intercept('GET', '/api/votes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', votePageUrlPattern);

        vote = undefined;
      });
    });
  });

  describe('new Vote page', () => {
    beforeEach(() => {
      cy.visit(`${votePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Vote');
    });

    it('should create an instance of Vote', () => {
      cy.get(`[data-cy="voteType"]`).type('couch');
      cy.get(`[data-cy="voteType"]`).should('have.value', 'couch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vote = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', votePageUrlPattern);
    });
  });
});
