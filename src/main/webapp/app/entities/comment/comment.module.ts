import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OnlineTutorSharedModule } from 'app/shared';
import {
  CommentComponent,
  CommentDetailComponent,
  CommentUpdateComponent,
  CommentDeletePopupComponent,
  CommentDeleteDialogComponent,
  commentRoute,
  commentPopupRoute
} from './';

const ENTITY_STATES = [...commentRoute, ...commentPopupRoute];

@NgModule({
  imports: [OnlineTutorSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CommentComponent,
    CommentDetailComponent,
    CommentUpdateComponent,
    CommentDeleteDialogComponent,
    CommentDeletePopupComponent
  ],
  entryComponents: [CommentComponent, CommentUpdateComponent, CommentDeleteDialogComponent, CommentDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OnlineTutorCommentModule {}
