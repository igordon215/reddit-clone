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

describe('Subreddit e2e test', () => {
  const subredditPageUrl = '/subreddit';
  const subredditPageUrlPattern = new RegExp('/subreddit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subredditSample = { name: 'until' };

  let subreddit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subreddits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subreddits').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subreddits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subreddit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subreddits/${subreddit.id}`,
      }).then(() => {
        subreddit = undefined;
      });
    }
  });

  it('Subreddits menu should load Subreddits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subreddit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Subreddit').should('exist');
    cy.url().should('match', subredditPageUrlPattern);
  });

  describe('Subreddit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subredditPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Subreddit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subreddit/new$'));
        cy.getEntityCreateUpdateHeading('Subreddit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subredditPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subreddits',
          body: subredditSample,
        }).then(({ body }) => {
          subreddit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subreddits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [subreddit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subredditPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Subreddit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subreddit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subredditPageUrlPattern);
      });

      it('edit button click should load edit Subreddit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subreddit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subredditPageUrlPattern);
      });

      it('edit button click should load edit Subreddit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subreddit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subredditPageUrlPattern);
      });

      it('last delete button click should delete instance of Subreddit', () => {
        cy.intercept('GET', '/api/subreddits/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subreddit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subredditPageUrlPattern);

        subreddit = undefined;
      });
    });
  });

  describe('new Subreddit page', () => {
    beforeEach(() => {
      cy.visit(`${subredditPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Subreddit');
    });

    it('should create an instance of Subreddit', () => {
      cy.get(`[data-cy="name"]`).type('mid');
      cy.get(`[data-cy="name"]`).should('have.value', 'mid');

      cy.get(`[data-cy="description"]`).type('a');
      cy.get(`[data-cy="description"]`).should('have.value', 'a');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subreddit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subredditPageUrlPattern);
    });
  });
});
