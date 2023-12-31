import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FileSystemFileEntry } from 'ngx-file-drop';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient: HttpClient) { }

  uploadVideo(fileEntry: File): Observable<any> {
    const formData = new FormData()
    formData.append('file', fileEntry , fileEntry.name)

    //HTTP host call to upload the video.
    return this.httpClient.post("http://localhost:8080/api/videos/uploadVideo",formData);

  }
}
