@createcontact
Feature: Create Contact

  Background:
    Given the user is registered to use the service


  Scenario Outline: Validate that a user can add a contact to a list of contacts
    Given the contact has a first name "<first_name>", a last name "<last_name>" and birthdate "<birth_date>"
    And the contact has an email address "<email>" and cellphone number "<cellphone>"
    And the contact has a physical address of "<address1>", "<address2>", "<city>" "<state>" "<postal_code>" "<country>"
    When the user adds a new contact
    Then the new contact is in a list of contacts
    Examples:
      | first_name | last_name | birth_date | email         | cellphone      | address1  | address2  | city   | state | country |postal_code |
      | Ken        | Smith     | 1970-01-01 | auto-generated| auto-generated | 1 Main St.|Apartment A| Anytown| KS    | USA     | 12345      |

  Scenario Outline: Validate that a user can delete a contact from a list of contacts
    Given three contacts are created
    And the user has a list of contacts
    When the user deletes the contact in position "<position>"
    Then the response message is "<msg>"
    Then the contact is not in a list of contacts
    Examples:
      | position   | msg            |
      | 1          | Contact deleted|